# WebLogCat
[ ![Download](https://api.bintray.com/packages/imob/maven/weblogcat/images/download.svg?version=0.0.1) ](https://bintray.com/imob/maven/weblogcat/0.0.1/link)

view no-truncated android logs on web

## dependencies
```
implementation 'com.koushikdutta.async:androidasync:2.+'
implementation 'com.imob:weblogcat:0.0.1'
```

## init
`WebLogCat.init(context);`

##view logs
visit `http://<your devices's local ip here>:8088` to view logs in your web browser. The lib will also log the full address to view logs during its initialization process. After the lib was inited, filter logs by tag `IMOB-WebLogCat`, then you'll get the address log.


##log

`WebLogCat.log(tag,msg,logLevel,throwable);`

##screenshots
![](md_res/screenshot.png)