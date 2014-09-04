simtest
=======

This is a c/c++ code similarity test tool, based on abstract syntax tree and tree edit distance.  The user interface is ugly  but not important :)   Hope you like it.

There is a little demo(demo.java) for the simtest. It shows how to use the simtest simply.

If you want to use simtest in a command line interface, I suggest you making the simtest into a jar and call simtest in the command line.

How to make the simtest project into a jar and call it in a command line interface?
1. Modify the "Main.java" ( look at demo.java )
2. Modify the "MANIFEST.MF" , set the Main-Class and Class-Path ( we need those two jars in the lib )
3. Export the project into jar using the configure of the "MANIFEST.MF"
4. Enjoy you journey in the simtest.