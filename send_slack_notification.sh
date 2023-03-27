#!/bin/bash

# Check the exit codes of previous steps
if [ "$1" -eq 0 ] && [ "$2" -eq 0 ]; then
    STATUS="SUCCESS"
else
    STATUS="FAILURE"
fi

# Send the appropriate message to Slack
curl -X POST \
     -H 'Content-Type: application/json' \
     -d "{\"buildId\":\"$BUILD_ID\",\"projectId\":\"$PROJECT_ID\",\"stepId\":\"Deploy Frontend and Backend\",\"status\":\"$STATUS\",\"isFinalStep\":true}" \
     'https://us-central1-blog-pipe.cloudfunctions.net/notifySlack'
