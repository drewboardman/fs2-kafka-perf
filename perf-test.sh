/usr/bin/kafka-producer-perf-test.sh \
  --topic ssl-perf-test \
  --throughput -1 \
  --num-records 3000000 \
  --record-size 1024 \
  --producer-props acks=all bootstrap.servers=localhost:9091 \
  # --producer.config /path/to/ssl-perf-test.properties
