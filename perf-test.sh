/usr/bin/kafka-producer-perf-test.sh \
  --topic ssl-perf-test \
  --throughput -1 \
  --num-records 1000000 \
  --record-size 556 \
  --producer-props acks=all bootstrap.servers=localhost:9091 \
  # --producer.config /path/to/ssl-perf-test.properties
