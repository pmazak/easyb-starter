ant = new AntBuilder()
def eclipseHome = System.console().readLine("\n\n> Enter your eclipse home directory: ")
tempPlugsDir = "eclipse"

eclipse_copy_jar = { file, folder ->
      if (file.isFile() && file.name.endsWith(".jar") && file.parent.contains(folder)) {
         ant.copy(file: file.absolutePath, todir: "$eclipseHome/$folder")
         println "  Copied $file.name to $eclipseHome/$folder"
      }
}
eclipse_copy_packageNamedFolder = { file, folder ->
   def isPackageNamedFolder = (file.name.split('\\.').length > 4)
   if (file.isDirectory() && isPackageNamedFolder && file.parent.endsWith("jars")) {
      ant.copy(todir: "$eclipseHome/$folder/$file.name") {
         fileset(dir: file.absolutePath)
      }
      println "  Copied $file.name to $eclipseHome/$folder"
   }
}
def install() {
	println new File(tempPlugsDir).absolutePath
	new File(tempPlugsDir).eachFile { download ->	
          if (download.file) {
			println ":plugin $download.name"
			ant.unzip(src: download, dest: "$tempPlugsDir/${download.name}X")
			new File(download.parent).eachFileRecurse {
			eclipse_copy_jar(it, "features")
			eclipse_copy_jar(it, "plugins")         
			eclipse_copy_packageNamedFolder(it, "plugins")
		  }
      }
   }
   println ("Now re-open eclipse!")
}
install()