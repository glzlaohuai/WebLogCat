# WebLogCat
[ ![Download](https://api.bintray.com/packages/imob/maven/weblogcat/images/download.svg?version=0.0.1) ](https://bintray.com/imob/maven/weblogcat/0.0.1/link)

view no-truncated android logs in web browser

## Dependencies
```
implementation 'com.koushikdutta.async:androidasync:2.+'
implementation 'com.imob:weblogcat:0.0.1'
```

## Initialize
`WebLogCat.init(context);`

## View Logs In Web Browser
visit `http://<your devices's local ip here>:8088` to view logs in your web browser. The lib will also log the full address during its initialization process. After the lib was inited, filter logs by tag `IMOB-WebLogCat`, then you'll get the address log.


## Print Log

`WebLogCat.log(tag,msg,logLevel,throwable);`

`logLevel` refers to the log priority constants definied in class:[`android.util.Log`](https://developer.android.com/reference/android/util/Log#constants_1)

## Quick Demo

[Demo](https://github.com/glzlaohuai/WebLogCat/releases/download/0.0.1/demo.apk)

## ScreenShots
![](md_res/screenshot.png)
