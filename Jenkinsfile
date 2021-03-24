pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        dir('CompactDiscDaoWithRestAndBoot') {
          withMaven (
            maven: 'mvn363',
            mavenLocalRepo: '.repository',
          ){
            sh 'ls -l'
            sh 'mvn -Dmaven.test.skip=true clean package'
          }
        }
      }
    }
    stage('Test') {
      steps {
        dir('CompactDiscDaoWithRestAndBoot') {
          withMaven {
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
