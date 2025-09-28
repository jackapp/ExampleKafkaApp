curl -X POST http://localhost:8081/subjects/user-value/versions \
  -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  -d '{
  "schema": "{ \"type\": \"record\", \"namespace\": \"com.example\", \"name\": \"User\", \"fields\": [ { \"name\": \"id\", \"type\": \"long\" }, { \"name\": \"name\", \"type\": \"string\" } ] }"
}'