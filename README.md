# jMutOps

jMutOps is a Java tool that I developed during my computer science bachelor thesis at Saarland University in 2013. It has the capabilities to map detected source code changes between two different Java source code versions to 63 different mutation operators retrieved from research papers and existing mutation testing tools. The goal of my thesis was to contribute to the question whether mutation testing could be used to generate real-life bugs.

First, an abstract syntax tree (AST) is generated with help of the *Eclipse JDT parser*. Next, the research tool *ChangeDistiller* is used to calculate the syntactic difference between two versions of the same file based on an existing algorithm by Chawathe et al. for change detection in hierarchically structured information. The changes are remapped on the AST where one visitor pattern is applied for each mutation operator to recognize whether the detected changes are matched to the operator's distinct behavior. For my evaluation, I ran jMutOps on various bugfixes of *AspectJ*. I found that 
1. only a dozen of mutation operators were recognized,
2. most of the described mutation operators could not be matched to the changes in the test set, and
3. most of the changes were not matched to mutation operators. 

For more details to the tool and results, please read my bachelor thesis "jMutOps: Mapping Changes to Mutation Operators".

## Credits
* [ChangeDistiller](https://bitbucket.org/sealuzh/tools-changedestiller/) by Software Evolution and Architecture Lab - University of Zurich
* [Eclipse JDT](http://eclipse.org/jdt/overview.php) by Eclipse Foundation
* [Apache Commons IO](https://commons.apache.org/io/) by Apache Foundation
* [iBugs - AspectJ](https://www.st.cs.uni-saarland.de/ibugs/) by Software Engineering Chair - Saarland University

## Acknowledgement
See acknowledgement section in my thesis.

## License
jMutOps is licensed under the [MIT license](https://github.com/lsubel/jmutops/blob/master/LICENSE.txt).
