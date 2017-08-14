I didn't make calculation part because I didn't understand calculation method.
I made only weekend check part.
I used spring batch for csv analyzing. 
It should be better spark and spring boot together I guess. 
I read some stackowerflow post about spring stuck over gb of data. But spark can handle over tb of data.

Here is the part I couldn't understand;

![alt text](https://github.com/huseyinjkilic/spring-batch/blob/master/images/definition.png)


For instrument1 it says mean of all the values.

I understand I will look at the database. For example for first value;

INSTRUMENT1, 23-FEB-2010, 12.21 

And let say in database there is multiplier factor forinstrument1

![alt text](https://github.com/huseyinjkilic/spring-batch/blob/master/images/example_database.png)

This means I should multiply with 0.5 * 12.21.
After this, it says for instrument1 mean of all values
It means all instrument1 or all values ??

![alt text](https://github.com/huseyinjkilic/spring-batch/blob/master/images/example_data.png)

For first 3 lines, What should be output ?

There is runnable jar file for only read data and check if it weekend or not.
The jar file is under https://github.com/huseyinjkilic/spring-batch/tree/master/target
You can run this jar file in command line java -jar huseyin-batch-processing-0.1.0.jar
You need java 8 for this jar file.

