package io.mosip.kafka.connect.transforms;

import org.apache.kafka.common.cache.Cache;
import org.apache.kafka.common.cache.LRUCache;
import org.apache.kafka.common.cache.SynchronizedCache;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.transforms.Transformation;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;

// import org.apache.http.HttpHost;
// import org.elasticsearch.client.RestHighLevelClient;
// import org.elasticsearch.client.RestClient;
// import org.elasticsearch.client.RequestOptions;
// import org.elasticsearch.action.search.SearchRequest;
// import org.elasticsearch.action.search.SearchResponse;
// import org.elasticsearch.search.SearchHit;
// import org.elasticsearch.search.builder.SearchSourceBuilder;
// import org.elasticsearch.index.query.QueryBuilders;
// import org.elasticsearch.index.query.BoolQueryBuilder;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.io.IOException;

public abstract class DynamicNewField<R extends ConnectRecord<R>> implements Transformation<R> {

    private abstract class Config{
        String type;
        String[] inputFields;
        String[] inputDefaultValues;
        String outputField;
        Schema outputSchema;
        Config(String type, String[] inputFields, String[] inputDefaultValues, String outputField, Schema outputSchema){
            this.type = type;
            this.inputFields = inputFields;
            this.inputDefaultValues = inputDefaultValues;
            this.outputField = outputField;
            this.outputSchema = outputSchema;
        }
        Object make(Object input){
            return null;
        }
        List<Object> makeList(Object input){
            return null;
        }
        void close(){
        }
    }
    private class ESQueryConfig extends Config{
        String esUrl;
        String esIndex;
        String[] esInputFields;
        String esOutputField;

        // RestHighLevelClient esClient;
        CloseableHttpClient hClient;
        HttpGet hGet;

        ESQueryConfig(String type, String esUrl, String esIndex, String[] esInputFields, String esOutputField, String[] inputFields, String[] inputDefaultValues,String outputField) {
            super(type,inputFields,inputDefaultValues,outputField,Schema.STRING_SCHEMA);

            this.esUrl=esUrl;
            this.esIndex=esIndex;
            this.esInputFields=esInputFields;
            this.esOutputField=esOutputField;

            // esClient = new RestHighLevelClient(RestClient.builder(HttpHost.create(this.esUrl)));
            hClient = HttpClients.createDefault();
            hGet = new HttpGet(this.esUrl+"/"+this.esIndex+"/_search");
            hGet.setHeader("Content-type", "application/json");
        }

        Object makeQuery(List<Object> inputValues){
            if(inputValues.size()!=inputFields.length){
                return "Cant get all values for the mentioned " + INPUT_FIELDS_CONFIG + ". Given " + INPUT_FIELDS_CONFIG + " : " + Arrays.toString(inputFields)+ " " + inputValues;
            }
            else if(inputValues.size()==0){
                return null;
            }

            String requestJson = "{\"query\": { \"bool\": { \"must\": [";

            for(int i=0; i<inputFields.length; i++){
                if(i!=0)requestJson+=",";
                requestJson += "{\"term\": {\"" + esInputFields[i] + ".keyword\": \"" + inputValues.get(i) + "\"}}";
            }
            requestJson += "]}}}";
            
            hGet.setEntity(new StringEntity(requestJson));

            JSONObject responseJson;

            final int MAX_RETRIES = 5;
            for(int i=1; i <= MAX_RETRIES; i++){
                try(CloseableHttpResponse hResponse = hClient.execute(hGet)){
                    HttpEntity entity = hResponse.getEntity();
                    String jsonString = EntityUtils.toString(entity);
                    responseJson = new JSONObject(jsonString);
                }
                catch(Exception e){
                    if(i==MAX_RETRIES) return "Error occured while making the query : " + e.getMessage();
                    else continue;
                }

                // if(responseJson.getJSONObject("hits").getJSONArray("hits").length()!=0){
                try{
                    // get the top hit .. error handling not done properly
                    return responseJson.getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source").getString(esOutputField);
                }
                catch(JSONException je){
                    if(i==MAX_RETRIES) return "Error: No hits found";
                    else continue;
                }
            }
            // control shouldn't reach here .. it shouldve thrown exception before or returned
            return "EMPTY";

        }

        List<Object> makeQueryForList(List<Object> inputValues){

            int arraySize = -1;
            for(Object v : inputValues){
                if(v instanceof List){
                    if(arraySize == -1) arraySize = ((List<Object>)v).size();
                    else if(arraySize != ((List<Object>)v).size()) throw new DataException("Irregular Array List Sizes");
                }
            }
            List<Object> input = new ArrayList<Object>();
            List<Object> output = new ArrayList<Object>();

            for(int j = 0; j < arraySize; j++){
                List<Object> list = new ArrayList<Object>();
                for(int i = 0; i < inputValues.size(); i++){

                    if(inputValues.get(i) instanceof List){
                        list.add(((List<Object>)inputValues.get(i)).get(j));
                    }
                    else{
                        list.add(inputValues.get(i));
                    }
                }
                input.add(list);

            }

            for(Object v : input){
                output.add(make(v));
            }

            return output;
        }
        // Object makeQuery(List<Object> inputValues){
        //     if(inputValues.size()!=inputFields.length){
        //         return "Cant get all values for the mentioned " + INPUT_FIELDS_CONFIG + ". Given " + INPUT_FIELDS_CONFIG + " : " + inputFields;
        //     }
        //     else if(inputValues.size()==0){
        //         return null;
        //     }
        //     // todo
        //     SearchRequest searchRequest = new SearchRequest();
        //     searchRequest.indices(esIndex);
        //     SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //     BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //     for(int i=0; i<inputFields.length; i++){
        //         boolQueryBuilder.must(QueryBuilders.termQuery(esInputFields[i], inputValues.get(i)));
        //     }
        //     sourceBuilder.query(boolQueryBuilder);
        //     searchRequest.source(sourceBuilder);
        //
        //     SearchResponse searchResponse;
        //     final int MAX_RETRIES = 5;
        //     for(int i=1; i <= MAX_RETRIES; i++){
        //         try{
        //             searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        //         }
        //         catch(Exception e){
        //             if(i==MAX_RETRIES) return "Error occured while making the query : " + e.getMessage();
        //             else continue;
        //         }
        //
        //         // get the top hit .. error handling not done properly
        //         try{
        //             SearchHit[] hits = searchResponse.getHits().getHits();
        //             return hits[0].getSourceAsMap().get(esOutputField);
        //         }
        //         catch(Exception e){
        //             if(i==MAX_RETRIES) return "Error occured after querying, while getting the new field : " + e.getMessage();
        //             else continue;
        //         }
        //     }
        //     // control shouldn't reach here .. it shouldve thrown exception before or returned
        //     return "EMPTY";
        // }
        
        @Override
        Object make(Object input){
            return this.makeQuery((List<Object>)input);
        }

        @Override
        List<Object> makeList(Object input){
            return this.makeQueryForList((List<Object>)input);
        }

        @Override
        void close(){
            // try{ esClient.close(); }catch(Exception e){}
            try{hClient.close();}catch(Exception e){}
        }

    }

    public static final String PURPOSE = "dynamic field insertion";
    public static final String TYPE_CONFIG = "query.type";
    public static final String ES_URL_CONFIG = "es.url";
    public static final String ES_INDEX_CONFIG = "es.index";
    public static final String ES_INPUT_FIELDS_CONFIG = "es.input.fields";
    public static final String ES_OUTPUT_FIELD_CONFIG = "es.output.field";
    public static final String INPUT_FIELDS_CONFIG = "input.fields";
    public static final String OUTPUT_FIELD_CONFIG = "output.field";
    public static final String DEFAULT_VALUE_CONFIG = "input.default.values";

    private Config config;
    private Cache<Schema, Schema> schemaUpdateCache;

    public static ConfigDef CONFIG_DEF = new ConfigDef()
        .define(TYPE_CONFIG, ConfigDef.Type.STRING, "es", ConfigDef.Importance.HIGH, "This is the type of query made. For now this field is ignored and defaulted to es")
        .define(ES_URL_CONFIG, ConfigDef.Type.STRING, "", ConfigDef.Importance.HIGH, "Installed Elasticsearch URL")
        .define(ES_INDEX_CONFIG, ConfigDef.Type.STRING, "", ConfigDef.Importance.HIGH, "Name of the index in ES to search")
        .define(ES_INPUT_FIELDS_CONFIG, ConfigDef.Type.STRING, "", ConfigDef.Importance.HIGH, "ES documents with given input field will be searched for. This field tells the key name")
        .define(ES_OUTPUT_FIELD_CONFIG, ConfigDef.Type.STRING, "", ConfigDef.Importance.HIGH, "If a successful match is made with the above input field+value, the value of this output field from the same document will be returned")
        .define(INPUT_FIELDS_CONFIG, ConfigDef.Type.STRING, "", ConfigDef.Importance.HIGH, "Name of the field in the current index")
        .define(OUTPUT_FIELD_CONFIG, ConfigDef.Type.STRING, "", ConfigDef.Importance.HIGH, "Name to give to the new field")
        .define(DEFAULT_VALUE_CONFIG, ConfigDef.Type.STRING, "", ConfigDef.Importance.HIGH, "Default vlaues for input fields");


    @Override
    public void configure(Map<String, ?> configs) {
        AbstractConfig absconf = new AbstractConfig(CONFIG_DEF, configs, false);

        schemaUpdateCache = new SynchronizedCache<>(new LRUCache<Schema,Schema>(16));

        String type = "es";

        if(type.equals("es")){
            String esUrl = absconf.getString(ES_URL_CONFIG);
            String esIndex = absconf.getString(ES_INDEX_CONFIG);
            String esInputFieldBulk = absconf.getString(ES_INPUT_FIELDS_CONFIG);
            String esOutputField = absconf.getString(ES_OUTPUT_FIELD_CONFIG);
            String inputFieldBulk = absconf.getString(INPUT_FIELDS_CONFIG);
            String outputField = absconf.getString(OUTPUT_FIELD_CONFIG);
            String inputDefaultValuesBulk = absconf.getString(DEFAULT_VALUE_CONFIG);

            if(type.isEmpty() || esUrl.isEmpty() || esIndex.isEmpty() || esInputFieldBulk.isEmpty() || esOutputField.isEmpty() || inputFieldBulk.isEmpty() || outputField.isEmpty() || inputDefaultValuesBulk.isEmpty()){
                throw new ConfigException("One of required transform config fields not set. Required field in tranforms: " + ES_URL_CONFIG + " ," + ES_INDEX_CONFIG + " ," + ES_INPUT_FIELDS_CONFIG + " ," + ES_OUTPUT_FIELD_CONFIG + " ," + INPUT_FIELDS_CONFIG + " ," + OUTPUT_FIELD_CONFIG + " ," + DEFAULT_VALUE_CONFIG);
            }

            String[] inputFields = inputFieldBulk.replaceAll("\\s+","").split(",");
            String[] esInputFields = esInputFieldBulk.replaceAll("\\s+","").split(",");
            String[] inputDefaultValues = inputDefaultValuesBulk.replaceAll("\\s+","").split(",");

            if(inputFields.length != esInputFields.length || inputFields.length != inputDefaultValues.length){
                throw new ConfigException("No of " + INPUT_FIELDS_CONFIG + " and no of " + ES_INPUT_FIELDS_CONFIG + "and number of " + DEFAULT_VALUE_CONFIG + " doesnt match. Given " + INPUT_FIELDS_CONFIG + ": " + inputFieldBulk + ". Given " + ES_INPUT_FIELDS_CONFIG + ": " + esInputFieldBulk + ". Given " + DEFAULT_VALUE_CONFIG + ": " + inputDefaultValuesBulk);
            }

            try{
                config = new ESQueryConfig(type,esUrl,esIndex,esInputFields,esOutputField,inputFields,inputDefaultValues,outputField);
            }
            catch(Exception e){
                throw new ConfigException("Can't connect to ElasticSearch. Given url : " + esUrl + " Error: " + e.getMessage());
            }
        }
        else{
            throw new ConfigException("Unknown Type : " + type + ". Available types now: \"es\"" );
        }
    }

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public void close() {
        schemaUpdateCache = null;
    }

    @Override
    public R apply(R record) {
        if (operatingValue(record) == null) {
            return record;
        } else if (operatingSchema(record) == null) {
            return applySchemaless(record);
        } else {
            return applyWithSchema(record);
        }
    }

    protected abstract Schema operatingSchema(R record);

    protected abstract Object operatingValue(R record);

    protected abstract R newRecord(R record, Schema updatedSchema, Object updatedValue);

    public static class Key<R extends ConnectRecord<R>> extends DynamicNewField<R> {
        @Override
        protected Schema operatingSchema(R record) {
            return record.keySchema();
        }

        @Override
        protected Object operatingValue(R record) {
            return record.key();
        }

        @Override
        protected R newRecord(R record, Schema updatedSchema, Object updatedValue) {
            return record.newRecord(record.topic(), record.kafkaPartition(), updatedSchema, updatedValue, record.valueSchema(), record.value(), record.timestamp());
        }
    }

    public static class Value<R extends ConnectRecord<R>> extends DynamicNewField<R> {
        @Override
        protected Schema operatingSchema(R record) {
            return record.valueSchema();
        }

        @Override
        protected Object operatingValue(R record) {
            return record.value();
        }

        @Override
        protected R newRecord(R record, Schema updatedSchema, Object updatedValue) {
            return record.newRecord(record.topic(), record.kafkaPartition(), record.keySchema(), record.key(), updatedSchema, updatedValue, record.timestamp());
        }
    }


    private R applySchemaless(R record) {
        final Map<String, Object> value = Requirements.requireMap(operatingValue(record), PURPOSE);

        final Map<String, Object> updatedValue = new HashMap<>(value);

        List<Object> valueList = new ArrayList<Object>();
        boolean nullValues = false;
        boolean dealingWithList = false;
        for(int i = 0; i < config.inputFields.length; i++){
            Object v = Requirements.getNestedField(value,config.inputFields[i]);
            if(v!=null){
                valueList.add(v);
                if(v instanceof List<?>) dealingWithList = true;
            }
            else{
                if(!config.inputDefaultValues[i].equals("null")){
                    valueList.add(config.inputDefaultValues[i]);                    
                }
                else{
                    nullValues = true;
                    break;
                }
            }
        }
        if(!nullValues && !dealingWithList) updatedValue.put(config.outputField, config.make(valueList));
        else if(!nullValues && dealingWithList) updatedValue.put(config.outputField, config.makeList(valueList));

        return newRecord(record, null, updatedValue);
    }

    private R applyWithSchema(R record) {
        final Struct value = Requirements.requireStruct(operatingValue(record), PURPOSE);

        Schema updatedSchema = schemaUpdateCache.get(value.schema());
        if (updatedSchema == null) {
            updatedSchema = makeUpdatedSchema(value.schema());
            schemaUpdateCache.put(value.schema(), updatedSchema);
        }

        final Struct updatedValue = new Struct(updatedSchema);

        for (Field field : value.schema().fields()) {
            updatedValue.put(field.name(), value.get(field));
        }

        List<Object> valueList = new ArrayList<Object>();
        for(String field : config.inputFields){
            Object v = ((Object[])Requirements.getNestedField(value,field))[0];
            // v is expected to be a string, case of List dealt in applySchemaless()
            if(v!=null) valueList.add(v);

        }
        updatedValue.put(config.outputField, config.make(valueList));

        return newRecord(record, updatedSchema, updatedValue);
    }
    private Schema makeUpdatedSchema(Schema schema) {
        final SchemaBuilder builder = SchemaUtil.copySchemaBasics(schema, SchemaBuilder.struct());

        for (Field field : schema.fields()) {
            builder.field(field.name(), field.schema());
        }

        builder.field(config.outputField, config.outputSchema);

        return builder.build();
    }
    

}
