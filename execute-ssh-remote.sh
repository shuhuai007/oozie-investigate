

jar_dir=$(ls lib)

for file in $jar_dir; do 
    echo $file; 
    classpath=$classpath:lib/$file;
    echo $classpath; 
done

java -classpath $classpath com.zhoujie.SSHExecution $1 $2 $3
