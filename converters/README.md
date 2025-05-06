# Converters

This project has the implementation for biometric and document converters.

## Channel:
Default channel is HTTP (POST)
## Request:
```
{
  "id": "sample-converter",
  "version": "1.0",
  "requesttime": "2022-02-22T16:46:09.499Z",
  "request": {
    "values": {
      "Left Thumb": "<base64 url encoded bdb>",
      "Right Iris": "<base64 url encoded bdb>"
    },
    "sourceFormat": "string",
    "targetFormat": "string",
    "sourceParameters": {
      "key": "value"
    },
    "targetParameters": {
      "key": "value"
    }
  }
}
```

| Property | Description |
| -------- | ------------|
| values | key-value pairs, with base64 url encoded data |
| sourceFormat | Http mime types, ISO formats |
| sourceParameters | key-value pairs |
| targetFormat |  Http mime types, ISO formats |
| targetParameters | key-value pairs |

## Response:
```
{
  "id": "sample-converter",
  "version": "1.0",
  "responsetime": "2022-02-22T16:46:09.513Z",
  "errors": [
    {
      "errorCode": "string",
      "errorMessage": "string"
    }
  ],
  "response": {
    "Left Thumb": "<base64 url encoded converted data>",
    "Right Iris": "<base64 url encoded converted data>"
  }
}
```

| Property | Description |
| -------- | ------------|
| response | key-value pairs, with base64 url encoded converted data |

## Error-codes:

|     Code    |          Description                  |
| ------------| --------------------------------------|
| MOS-CNV-001 | Conversion source format not supported|
| MOS-CNV-002 | Conversion target format not supported|
| MOS-CNV-003 | Invalid source value|
| MOS-CNV-500 | Technical Error|



