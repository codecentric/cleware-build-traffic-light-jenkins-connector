# Build Traffic Light (BTL) Jenkins Connector

This application is a (very) simple connector for Cleware GmbH's traffic light. You can use this application to connect multiple Jenkins builds to the traffic light.

## Usage

 1. Install [Leiningen](https://github.com/technomancy/leiningen)
 2. Configure the application through src/btl/core.clj
 3. Execute the following command: lein uberjar
 4. Run the application: java -jar *standalone.jar

## License

Copyright (C) 2013 codecentric AG

Distributed under the MIT License.
