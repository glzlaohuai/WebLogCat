# WebLogCat
[ ![Download](https://api.bintray.com/packages/imob/maven/weblogcat/images/download.svg?version=0.0.1) ](https://bintray.com/imob/maven/weblogcat/0.0.1/link)

view no-truncated android logs on web

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

[Demo](https://github-production-release-asset-2e65be.s3.amazonaws.com/312521669/7de35300-2840-11eb-8f9e-d7c53e549830?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20201116%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20201116T112018Z&X-Amz-Expires=300&X-Amz-Signature=8012f5e4e83759e39440e722a0820a034fb37043470c334832cc6d6737f42563&X-Amz-SignedHeaders=host&actor_id=2596885&key_id=0&repo_id=312521669&response-content-disposition=attachment%3B%20filename%3Ddemo.apk&response-content-type=application%2Fvnd.android.package-archive)

## ScreenShots
![](md_res/screenshot.png)
