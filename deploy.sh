#!/bin/bash
npm run build --prefix ./GUI # prefix erases dir, can use && cp instead
mvn install clean -f ./backend/pom.xml
cat <<EOF >./target/.htaccess
Options -MultiViews
RewriteEngine On
RewriteCond %{REQUEST_FILENAME} !-f
RewriteRule ^ index.html [QSA,L]
EOF
#sshpass -p 'OR-studentui2203' scp -P 22265 -v -r target/* rokas@isk.ktu.lt:/var/www/l-s1/public_html/
sshpass -p 'OR-studentui2203' rsync -a -P -e "ssh -p 22265" /home/asd/IdeaProjects/Bakalauras/target/ rokas@isk.ktu.lt:/var/www/l-s1/public_html/
sshpass -p 'OR-studentui2203' ssh rokas@isk.ktu.lt -p 22265 'pkill -9 -u rokas java && java -Dspring.profiles.active=prod -jar /var/www/l-s1/public_html/backend.jar' 
echo Willass98 | sudo openconnect vpn.ktu.lt --user=rokmoc --passwd-on-stdin --usergroup=internet &
