#!/bin/sh

# Run the application
java -cp "/opt/studiesandme-backend-exercise/lib/*" -XX:+UseContainerSupport -XX:MaxRAMPercentage=80.0 com.studiesandme.backend.Main
