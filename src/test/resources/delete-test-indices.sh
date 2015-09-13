!#/bin/sh

curl -XDELETE "http://localhost:9200/test-logstash-2015.09.03?pretty"
curl -XDELETE "http://localhost:9200/test-logstash-2015.09.12?pretty"
curl -XDELETE "http://localhost:9200/test-logstash-2015.09.13?pretty"
curl -XDELETE "http://localhost:9200/test-logstash-2015.08.23?pretty"
curl -XDELETE "http://localhost:9200/test-logstash-2015.08.24?pretty"
