mvn clean package
rm lib/SSHSample*jar
cp target/*jar lib

rm workflow/lib
cp -r lib workflow
