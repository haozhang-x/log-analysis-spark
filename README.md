# Structured Streaming Log Analysis

## Project Introduction
Use Python to simulate a website log and send the log file to kafka's message.
Use Spark Structured Streaming to process the log data in kafka to calculate the total PV, the PV of each IP, the PV of the search engine, the PV of the keyword, the PV of the terminal, and write the final result to the RDBMS.

## The log file is sent to kafka's message.
sample_web_log.py use to generate logs
You can use the following commands to produce kafka's message
```bash
python sample_web_log.py|kafka-console-producer.sh --broker-list your_broker_list --topic  your_topic  
```

You can also use the crontab to generate kafka messages at regular intervals.
```bash
crontab -e
0/5 * * * * ? python sample_web_log.py|kafka-console-producer.sh --broker-list your_broker_list --topic  your_topic 
```
 
 
 