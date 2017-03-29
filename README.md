---
layout: default
---
# [Email Crawler](https://jasonbroker.github.io/EasyCrawler/)

*The email crawler is a modern designed GUI crawler for batch fetching useful infomation on the website. Not only for emails but other keywords are also supported.*

## Usage

This project is developed in java, which need jvm installed. JRE [Download here](https://www.java.com/en/download/manual.jsp) and select the right package.

### Kick to start

Note that unzip is always the first step after the zip package has been downloaded.

##### For Windows:

Double click the file named `start_from_here.bat`, It will call a black window out and a GUI software will be opened then.

##### For Mac and linux:

Double click the jar file named `crawler_main.jar`(might vary in different version).

### Make it work:

 It contains 3 sections of function zone.

#### Email crawler
 
 ![MacDown Screenshot](./ScreenShots/preview1.png)
 
 1. Type in the root url or it's links which contains the email info mation.
 2. The url is 2 types: normal type and indexing. normal one is normal as it is. The indexing url is like `http://abc.com/web?studentpage=1`. The number `1` could be replaced by other number and then we can see the other pages there. the max number is always shown on the page.
 3. Crawl speed is the thread number of crawling. Under 100 is safe, while over that value might lead to lagging.
 4.  The inner hyper link will never be fetched so that `strict mode` is very useful for speed up crawling. But if you need to crawl links in the web page, the crawling mode should be closed.
 5. prefix and sufix space and enter is recommonded commonly. This will avoid some common problems which might lead to crawling failure.
 6. The replacement area is the key feature in the software. You can customize keyword to crawl revealed great extention ablity. In some web page `@` is replaced by `[@]`, `[#]`, etc to avoid crawling, but here we can use this feature to replace the `@`. Just type in the keywords even `@` image: `[@],[#],<image='./at.jpeg'>` seperated by comma, all these email types can be recorded well.
 7. Debug mode is for mac, windows can see the debug infomation on the black concle.
 8. Press the start button to start. It will automatically stop when finished.
 
#### Keyword crawler 
 
 ![MacDown Screenshot](./ScreenShots/preview2.png)
 
 This feature is more valuable than the others. Using this feature in proper way can reduce your work load greatly.
 It can search all relavent urls containing the keywords, helping you locate target painlessly.
 
#### Email Setting
 
 ![MacDown Screenshot](./ScreenShots/preview3.png)
 
 1. Select the place to save ur file!
 2. Read me contains **VERY IMPORTANT** information, please go through it.
 3. Don't forget to press Save.
 
#### Getting the product
 
The email(or anything you like if keyword is customized) will be generated on the Desktop(as default, you can change the directory in setting) with a the website’s url and a path extention `.csv.` e.g. `baidu.com.csv`.
The cells is seperated by comma and could be opened by excel(or some other supported office software).
[How to read the `file.csv` with column and lines on excel in Windows](http://jingyan.baidu.com/article/76a7e409bf9a3ffc3b6e1535.html)

## FAQ

1. Could it run on my mac, windows or Linux?
Yes. It is based on Java cross platform. Running on Android is also supported, but never try it on iOS devices.
2. I click the `.bat` file, then the black window said some error.
Most likely you forget install the java JRE.
3. Will this software charge?
NO. It is for free at all. No lock feature.
4. Developing Plan?
There is no developing plan. All version is build for specific demand.
5. How could I reset the settings?
All settings is stored in the software folder. Deleting the `.config` file shall work.
6. How to report bugs?
Open an [issue](https://github.com/Jasonbroker/emailcrawler/issues/new) for me. I will fix it ASAP.

## Roadmap

See the [open issues](https://github.com/pages-themes/cayman/issues) for a list of proposed features (and known issues).

## Project philosophy

email crawler is intended to make those people who need to find information's life easier. Thanks a lot to my friend *Li xu*, who strongly supported and supporting on this project. It should look great, but that goes without saying.

## Contributing

Interested in contributing? We'd love your help. Cayman is an open source project, built one contribution at a time by users like you. See [the CONTRIBUTING file](CONTRIBUTING.md) for instructions on how to contribute.

## Contact infomation

If you have any further question, please feel free to mail me or add my wechat `Zhou_zhengchang`.
