#!/usr/bin/env ruby

require "net/http"
require "uri"
require "json"

# configuration
jenkins = "http://<jenkins>:8080"
jobname = "<job name>"
uri = URI.parse(jenkins + "/job/" + jobname + "/api/json?pretty=true")
username = "user"
password = "password"
delay = 2.0

http = Net::HTTP.new(uri.host, uri.port)
request = Net::HTTP::Get.new(uri.request_uri)
request.basic_auth(username, password)

while true do
	response = http.request(request)
	
	if response.code == "200"
		result = JSON.parse(response.body)
		puts "#{result['color']}"
		
		# trigger traffic light
		
	else
		puts "A problem occurred. Response code is " + response.code
		break
	end
	sleep( delay )
end