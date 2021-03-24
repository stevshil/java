pipeline {
  agent any
  stages {
    stage('Clone') {
      steps{
        checkout([$class: 'GitSCM',
          branches: [[name: '*/master']],
          extensions: [
            [$class: 'RelativeTargetDirectory', relativeTargetDir: '.']
          ],
          userRemoteConfigs: [[url: 'https://github.com/stevshil/java.git']]
        ])
      }
    }
    stage('Build') {
      steps {
        dir('CompactDiscDaoWithRestAndBoot') {
          withMaven (
            maven: 'mvn363',
            mavenLocalRepo: '.repository',
          ){
            sh 'mvn -Dmaven.test.skip=true clean package'
          }
        }
      }
    }
    stage('Test') {
      steps {
        dir('CompactDiscDaoWithRestAndBoot') {
          withMaven (
            maven: 'mvn363',
            mavenLocalRepo: '.repository',
          ){
            sh 'mvn test'
          }
        }
      }
    }
    stage('Publish') {
      steps {
        echo 'Publish to our software repository'
      }
    }
  }
}
