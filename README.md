#Structured Streaming Log Analysis

## Project Introduction


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
 
 
 