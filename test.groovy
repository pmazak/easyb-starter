ant = new AntBuilder()
yourClassesUnderTestDir = '.'  // (../bin?)
yourAppLibDir = '.'            // (../lib?)
thisTestLibDir = 'lib'
outputReportFile = 'target/easyb-report/index.html'

ant.taskdef(name: 'easyb', classname: 'org.easyb.ant.BehaviorRunnerTask'){
	classpath {
	  fileset(dir: thisTestLibDir, includes: 'easyb*.jar')
	}
}
ant.path(id: 'testLibPath') {
	pathelement(location: yourClassesUnderTestDir)
    fileset(dir: yourAppLibDir, includes: '*.jar')
	fileset(dir: thisTestLibDir, includes: '*.jar')
}
def easyb() {
	   ant.mkdir(dir: 'target/easyb-report')
       ant.easyb() {
		 classpath(refid: 'testLibPath')
         report(location: outputReportFile, format:'html')
		 behaviors(dir: 'src'){
            include(name: '**/*Specification.groovy')
            include(name: '**/*Story.groovy')
         }
       }
	   println "Browse to " + new File(outputReportFile).toURI()
}
easyb()