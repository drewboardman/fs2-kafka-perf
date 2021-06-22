/usr/bin/kafka-topics.sh \
  --create \
  --topic ssl-perf-test \
  --partitions 5 \
  --replication-factor 1 \
  --config retention.ms=86400000 \
  --config min.insync.replicas=1 \
  --bootstrap-server localhost:9091 \
  # --command-config /path/to/ssl-perf-test.properties
