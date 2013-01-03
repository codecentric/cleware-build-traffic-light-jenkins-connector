# Build Traffic Light (BTL) Jenkins Connector

This repository contains scripts that can be used to control extreme 
feedback devices for the Jenkins Continuous Integration server.

There are several script that you can use, depending on the Version of your 
Jenkins and your preferred scripting language.


## REST API:

 - *Clojure Script*:  clojure/src/btl/core.clj
 - *Shell Script*:    bash/restApiListener.sh
 - *Ruby Script*:     ruby/restApi.rb


## RSS Feed:

 - *Shell Script*: bash/rssFeedReader.sh


# Software

To control the USB device from your command line, you need the software from Cleware:
 - http://www.cleware.net/download.html

For Linux use this link:
 - http://www.vanheusden.com/clewarecontrol/


# Have fun


# License

Copyright (C) 2013 codecentric AG

Distributed under the MIT License.