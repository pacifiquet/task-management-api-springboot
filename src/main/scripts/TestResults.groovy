def testResults = System.getProperty('testResults').split(',')
testResults.each { result ->
    println "Test: ${result.split(':')[0]} - Result: ${result.split(':')[1]}"
}