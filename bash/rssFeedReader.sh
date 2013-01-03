#!/bin/sh

# This script is used to control a USB Traffic Light from Cleware. You can buy 
# the USB Traffic Light from this shop: http://www.cleware-shop.de
# 
# The script uses the RSS FeedReader and can be used for older versions of Jenkins.
# In newer Versions of Jenkins, you can call the REST API to get the detailed
# information of every job.
#
# Requirements:
# 
#   The Cleware USB Traffic Light comes with a Control Software that you can 
#   download from the Cleware Website. 
#
#   This script can be run under Windows using a cygwin shell. You need to
#   have "curl" installed, so the script can poll the RSS Feed from the
#   Jenkins Job that you want to monitor.
#
#   This script has been tested under Windows running cygwin and Cleware4.2.8
#
# @MarcelBirkner
# 

# configure Jenkins Job + User/Password
JENKINS_URL="http://<jenkins>:8080/view/My%20Jobs/rssLatest"
JOB_NAME="<job name>"
CREDENTIALS=user:password

# cofigure script and time interval
CLEWARE_HOME=Cleware4.2.8
CLEWARE_COMMAND=./USBswitchCmd.exe
SLEEP_IN_SEC=1

cd $CLEWARE_HOME

# Loop that checks the Jenkins Job RSS Feed and updates the traffic light state
while true; do 

	state=`curl -u $CREDENTIALS $JENKINS_URL | sed 's/></>\n</g' | grep "<title>"$JOB_NAME | sed 's/(/ | /g' | sed 's/)/ | /g' | awk '{print $4}'`
	
	case $state in 
		?)      echo "State: Building    "`date`; $CLEWARE_COMMAND Y; sleep $SLEEP_IN_SEC; $CLEWARE_COMMAND G; sleep $SLEEP_IN_SEC;;
		stable) echo "State: Stable      "`date`; $CLEWARE_COMMAND G; sleep $SLEEP_IN_SEC;;
		back)   echo "State: Back        "`date`; $CLEWARE_COMMAND G; sleep $SLEEP_IN_SEC;;
		*)      echo "State: Instable    "`date`; $CLEWARE_COMMAND R; sleep $SLEEP_IN_SEC;;
	esac;
	
done;
