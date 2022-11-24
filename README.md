# MongoDBApp

This application aims at gaining a better understanding of traffic collision problems, identifying risk factors, and developing safe driving behaviors. This application offers users multiple options to explore and discover the data, including insert, update or delete the record, and generate accident statistics from different dimensions. The dataset used for analysis comes from the California Highway Patrol and covers collisions from 2009 to 2021 in California. Data size is about 1GB.

MongoDB is deployed in a cluster of 7 nodes on the AWS cloud, a sharding system consisting of 3 shards with each shard is replicated in a replica set of 3 members, one mongos, and a config server deployed in a replica set of 3 members. CASE_ID” is used as the shard key.
