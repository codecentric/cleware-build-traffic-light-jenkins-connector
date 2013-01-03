# Build Traffic Light (BTL) Jenkins Connector

This application is a (very) simple connector for Cleware GmbH's traffic light. You can use this application to connect two of your Jenkins builds to the traffic light.

The choice to go with two build jobs was made in order to watch out for our Sonar build job (static source code verification). The remainder of this document will refer to two kinds of build jobs:

 - *Commit Build Job*: This is the build job which is run each time you commit.
 - *Verification Build Job*: This might be a build job which runs your static source code verification, linting or integration tests.

## Usage

 1. Install [Leiningen](https://github.com/technomancy/leiningen)
 2. Configure the application through src/btl/core.clj
 3. Execute the following command: lein uberjar
 4. Run the application: java -jar *standalone.jar

## About the Lights

The lights will be turned on and off according to the following table.

<table>
  <thead>
    <tr>
      <th>Commit Build Job Status</th>
      <th>Verification Build Job Status</th>
      <th>Activated Light</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Success</td>
      <td>Success</td>
      <td>Green</td>
    </tr>
    <tr>
      <td>Success</td>
      <td>Warning</td>
      <td>Yellow</td>
    </tr>
    <tr>
      <td>Success</td>
      <td>Failure</td>
      <td>Yellow</td>
    </tr>
    <tr>
      <td>Warning</td>
      <td>Success</td>
      <td>Yellow</td>
    </tr>
    <tr>
      <td>Warning</td>
      <td>Warning</td>
      <td>Yellow</td>
    </tr>
    <tr>
      <td>Warning</td>
      <td>Failure</td>
      <td>Yellow</td>
    </tr>
    <tr>
      <td>Failure</td>
      <td>Success</td>
      <td>Red</td>
    </tr>
    <tr>
      <td>Failure</td>
      <td>Warning</td>
      <td>Red</td>
    </tr>
    <tr>
      <td>Failure</td>
      <td>Failure</td>
      <td>Red</td>
    </tr>
  </tbody>
</table>

## License

Copyright (C) 2013 codecentric AG

Distributed under the MIT License.
