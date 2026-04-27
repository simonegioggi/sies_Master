#!/bin/bash

# Script per cleanup e restart JBoss e IMQ
# Eseguire con: sudo ./cleanup_jboss.sh

echo "=== Stopping JBoss service ==="
cd /etc/rc.d/init.d
service jboss-as-standalone.sh stop
service jboss-as-standalone.sh status

echo ""
echo "=== Stopping IMQ ==="
./imq stop
./imq status

echo ""
echo "=== Cleaning SIES directories ==="
# Esegue comandi come utente SIES
su - SIES -c "cd /home/SIES/jboss-eap-6.4.Alpha/standalone && rm -rf data log tmp && ls -l"

echo ""
echo "=== Cleaning deployments ==="
su - SIES -c "cd /home/SIES/jboss-eap-6.4.Alpha/standalone/deployments && rm -rf *.deployed sies_*.war && ls -l"

echo ""
echo "=== Starting IMQ ==="
cd /etc/rc.d/init.d
./imq start
./imq status

echo ""
echo "=== Starting JBoss service ==="
service jboss-as-standalone.sh start
service jboss-as-standalone.sh status

echo ""
echo "=== Script completed ==="
