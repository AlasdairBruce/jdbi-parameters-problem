# Problem Processing Bridged Methods In JDBI When Using JDK8 -parameters Option

This little test shows how using a 
[bridged method](https://docs.oracle.com/javase/tutorial/java/generics/bridgeMethods.html) in combination with
the Java 8 ```-parameters``` option causes a failure.

How to show the failure...

```
bash$ mvn -Dwith-parameters clean test
...
...
java.lang.RuntimeException
	at org.skife.jdbi.asm.MethodVisitor.visitParameter(Unknown Source)
	at org.skife.jdbi.asm.ClassReader.b(Unknown Source)
	at org.skife.jdbi.asm.ClassReader.accept(Unknown Source)
	at org.skife.jdbi.asm.ClassReader.accept(Unknown Source)
	at org.skife.jdbi.cglib.proxy.BridgeMethodResolver.resolveAll(BridgeMethodResolver.java:61)
	at org.skife.jdbi.cglib.proxy.Enhancer.emitMethods(Enhancer.java:911)
	at org.skife.jdbi.cglib.proxy.Enhancer.generateClass(Enhancer.java:498)
	at org.skife.jdbi.cglib.core.DefaultGeneratorStrategy.generate(DefaultGeneratorStrategy.java:25)
	at org.skife.jdbi.cglib.core.AbstractClassGenerator.create(AbstractClassGenerator.java:216)
	at org.skife.jdbi.cglib.proxy.Enhancer.createHelper(Enhancer.java:377)
```

The failing code (decompiled) is:

```java
    public void visitParameter(String var1, int var2) {
        if(this.api < 327680) {
            throw new RuntimeException();
```

Running the same test, but omitting the -parameters command line argument to the java compiler works just fine:

```
bash$ mvn clean test
...
...
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 10.46 sec - in foo.TestFoo
```

This test works when using a locally built version of JDBI upgraded to include (shaded-ly) the following dependencies:

```
        <dependency>
            <groupId>cglib</groupId>
            <artifactId>cglib</artifactId>
            <version>3.2.2</version>
        </dependency>

        <dependency>
            <groupId>org.ow2.asm</groupId>
            <artifactId>asm</artifactId>
            <version>5.0.4</version>
        </dependency>
```
