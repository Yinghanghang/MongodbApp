# MongoDBApp

 A database application where MongoDB is deployed as sharded cluster on AWS cloud.
 MongoDB is deployed in a cluster of seven nodes.
 A config server is deployed in a replica set of three members.
 Three shards and each shard is replicated in a replica set of three members.
 Dataset populated into MongoDB is the Statewide Integrated Traffic Records System(SWITRS) with data that starts from 2002. These are the official vehicle collision reports with each collision having a case ID. 
 Information about the collision includes weather, road condition, people involved, vehicle type, number killed, etc… data size is about 1GB.
