def call(String repoUrl) {
  pipeline {
       agent any
       
       stages {
           
           stage("git") {
               steps {
                   git branch: 'master',
                       url: "${repoUrl}"
               }
           }
           stage('build') {
            steps {
                script {
                    def mvnhome = tool name: 'jenkins-maven', type: 'maven'
                    bat "${mvnhome}/bin/mvn clean install"
                }
            }

        }
        stage('deploy'){
            steps{
                nexusArtifactUploader artifacts: [[artifactId: 'my-app', classifier: '', file: 'C:\\Windows\\System32\\config\\systemprofile\\AppData\\Local\\Jenkins\\.jenkins\\workspace\\casestudy-1\\target\\my-app-1.0-SNAPSHOT.jar', type: 'jar']], credentialsId: 'cc14fffd-8917-4df8-b5dc-53a1207b4819', groupId: 'com.mycompany.app', nexusUrl: 'localhost:8081/', nexusVersion: 'nexus3', protocol: 'http', repository: 'maven-snapshots', version: '1.0-SNAPSHOT' }
        }
       }
    post {
failure {
  script {
    if (currentBuild.currentResult == 'FAILURE') {
      step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: "ananya.singh1080ti@gmail.com", sendToIndividuals: true])
    }
  }
}
success{
    script{
    if (currentBuild.currentResult == 'SUCCESS') {
      step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: "ananya.singh1080ti@gmail.com", sendToIndividuals: true])
    }
    }
}
  
}
   }
}
