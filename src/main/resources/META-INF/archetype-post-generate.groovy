System.out.println("Executing .gitignore hack")
file = new File( request.getOutputDirectory(), request.getArtifactId()+"/.gitignore.tmpl" );
def gitIgnorefile = new File( request.getOutputDirectory(), request.getArtifactId()+"/.gitignore" );
file.renameTo(gitIgnorefile)

System.out.println("Initializing git")
def initProcess = new ProcessBuilder("git","init").directory(new File(request.getArtifactId())).start()
initProcess.waitForProcessOutput(System.out, System.err)
def addProcess = new ProcessBuilder("git","add",".").directory(new File(request.getArtifactId())).start()
addProcess.waitForProcessOutput(System.out, System.err)
def commitProcess = new ProcessBuilder("git","commit","-m","chore: initial commit").directory(new File(request.getArtifactId())).start()
commitProcess.waitForProcessOutput(System.out, System.err)
def tagProcess = new ProcessBuilder("git","tag",request.getVersion()).directory(new File(request.getArtifactId())).start()
tagProcess.waitForProcessOutput(System.out, System.err)

System.out.println("Making mvnw executable")
def mvnwFile = new File( request.getOutputDirectory(), request.getArtifactId()+"/mvnw" );
mvnwFile.setExecutable(true,false)

def activeProfiles = request.getProjectBuildingRequest().getActiveProfileIds()
def isWindows = false
def isUnix = false
for (i=0;i<activeProfiles.size();i++) {
    if(activeProfiles[i].equals("windows-local-build"))
        isWindows=true
    if(activeProfiles[i].equals("unix-local-build"))
        isUnix=true
}

def mavenInstallProcess
if(isWindows && isUnix) {
    System.out.println("BOTH windows and UNIX profiles? Check your config...")
    System.exit(1)
}
else if(isWindows) {
    System.out.println("Executing 'mvn install -DskipTests=true' in directory "+request.getArtifactId())
    mavenInstallProcess = new ProcessBuilder("mvn.cmd","install","-DskipTests=true").directory(new File(request.getArtifactId())).start()
}
else if(isUnix) {
    System.out.println("Executing 'mvn install -DskipTests=true' in directory "+request.getArtifactId())
    mavenInstallProcess = new ProcessBuilder("mvn","install","-DskipTests=true").directory(new File(request.getArtifactId())).start()
}
else {
    System.out.println("NEITHER windows NOR UNIX profiles? Check your config...")
    System.exit(1)
}
mavenInstallProcess.waitForProcessOutput(System.out, System.err)

System.out.println("Updating git repo")
def addAgainProcess = new ProcessBuilder("git","add",".").directory(new File(request.getArtifactId())).start()
addAgainProcess.waitForProcessOutput(System.out, System.err)
def commitAgainProcess = new ProcessBuilder("git","commit","-m","feat: set up core features").directory(new File(request.getArtifactId())).start()
commitAgainProcess.waitForProcessOutput(System.out, System.err)

System.out.println("Executing final mvn package (for changelog to take effect)")
def mavenPackageProcess
if(isWindows && isUnix) {
    System.out.println("BOTH windows and UNIX profiles? Check your config...")
    System.exit(1)
}
else if(isWindows) {
    System.out.println("Executing 'mvn install -DskipTests=true' in directory "+request.getArtifactId())
    mavenPackageProcess = new ProcessBuilder("mvn.cmd","package","-DskipTests=true").directory(new File(request.getArtifactId())).start()
}
else if(isUnix) {
    System.out.println("Executing 'mvn install -DskipTests=true' in directory "+request.getArtifactId())
    mavenPackageProcess = new ProcessBuilder("mvn","package","-DskipTests=true").directory(new File(request.getArtifactId())).start()
}
else {
    System.out.println("NEITHER windows NOR UNIX profiles? Check your config...")
    System.exit(1)
}
mavenPackageProcess.waitForProcessOutput(System.out, System.err)

System.out.println("Done!")