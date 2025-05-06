# Masterdata Upload Utility

## Introduction
Masterdata is specified in xlsx files available in `xlsx` folder.  Update the xlsx as per the [masterdata guide](../../../docs/master_data_tables.xlsx) and run the upload utility. Note that you must run this utility only once to seed the DB data first time.  Subsequently, for any updates use Admin UI or Masterdata APIs.

## Prequisites
1. `python3.9` virtual env
1. Switch to virtual env
TODO: Check if below are needed
2. Install dev tools:
	```
	sudo apt install python3-dev libpq-dev
	```
3. Install additional modules:
	```
	pip install -r requirements.txt
	```
4. (Optional) Install [XLS diff utiliy](https://github.com/na-ka-na/ExcelCompare).

## Run
1. Checkout `mosip-data` repo at location of your choice:
    ```
    git clone https://github.com/mosip/mosip-data -b lts
    ```
1. 
    ```sh
    ./upload_md.sh <path of mosip-data repo>
    ```
    The shell script runs `lib/upload_masterdata.py` script.

1. To populate data only in specific tables comment out unwanted tables in `lib/table_order` with a `#` at the start of each line. 
