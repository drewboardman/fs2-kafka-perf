# Kafka Performance Test

This is a miniature/sample application that shows some performance issues with
fs2-kafka.

## Setting up the performance test
You need to have a few things installed to get the kafka perfomance test CLI
scripts.

Make sure you have `docker` and `docker-compose` installed and setup. You will also need some
performance scripts that come with [installing
kafka](https://kafka.apache.org/quickstart) on your computer. I think those
scripts may also come with installing `kafkactl`.

After you've cloned this repository, start up the zookeeper and kafka instances
locally with:

```
docker-compose up
```

Once these are running locally, create the kafka topic and partitions with:

```
./create-partition.sh
```

If this complains about the path to `/usr/bin/kafka-topics.sh` you probably just
need to change this to `/bin/...`.

Now you can get your benchmarks with 

```
./perf-test.sh
```

This will give you a baseline for how many events per second are being published
on your topic. The event size here is based on the average JSON payload size (in
Bytes) that I'm using in the sample application. If you replace the `Bag` you
may want to adjust this value as well to get a good comparison.

## Run application

Start your zookeeper/kafka stack with `docker-compose up` and run:

```shell
sbt run
```

This should publish 1 million (default) events and output how long it took.

## Some Results

`perf-test.sh` benchmarks

| Partitions | Replica Factor | Record Size (Bytes) | volume      | rec/sec average |
| ---------- | -------------- | ------------------- | ----------- | --------------- |
| 5          | 1              | 1024                | 3\_000\_000 | 113528          |
| 5          | 1              | 556                 | 1\_000\_000 | 138985          |
| 5          | 1              | 556                 | 1\_000\_000 | 174703          |

`fs2-kafka` application performance

| Parallelism | Linger (sec) | Batch Size  | Chunking | Partitions | Replica Factor | Record Size (Bytes) | volume      | rec/sec average | Efficiency WRT benchmark average @ 1\_000\_000 events |
| ----------- | ------------ | ----------- | -------- | ---------- | -------------- | ------------------- | ----------- | --------------- | ----------------------------------------------------- |
| default     | default      | default     | No       | 5          | 1              | ~560                | 1\_000\_000 | 5787            | 3.69%                                                 |
| default     | default      | default     | 1000     | 5          | 1              | ~560                | 1\_000\_000 | 18518           | 11.81%                                                |
| default     | default      | 1\_000\_000 | 1000     | 5          | 1              | ~560                | 1\_000\_000 | 19915           | 12.70%                                                |
| 30\_000     | default      | default     | 1000     | 5          | 1              | ~560                | 1\_000\_000 | 19149           | 12.21%                                                |
| 200         | default      | default     | 1000     | 5          | 1              | ~560                | 1\_000\_000 | 18711           | 11.93%                                                |
| default     | 2            | default     | 1000     | 5          | 1              | ~560                | 1\_000\_000 | 17562           | 11.20%                                                |
