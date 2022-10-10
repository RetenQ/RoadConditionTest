# RoadConditionTest
MIEC大一学期的cs183 路面平整度测试，用了地图+加速度传感器。很粗糙

## 欢迎借鉴研究！但是请不要照搬照抄
因为这个代码是非常非常基础的，其实一个人学个一个月左右就能写出来  
如果是MIEC的学生并且也准备完成CS183项目，请**不要照搬**，以防止出现不必要的意味  
如果您想借鉴思路实现，请参考“技术与学习”部分 

## 技术与学习  
1、安卓端的相关实现皆为AndroidStuio+Java完成，相关的教程其实参照官方文档即可。顺带一提《第一行代码》的第二版是java语言，后续版本是Kotlin语言  
2、数据库方面尚有bug，实现主要是使用了内置的数据库+SQL的链接。《第一行代码》里面有对应的讲解
3、地图API使用的是百度地图+天眼，相关使用方式在CSDN或者百度一下都有

## 写在最后 
这个项目不是我一个人完成是，是本人于大一学习期间，与项目团队（共四人）一同完成 
RetenQ：UI部分优化，集成项目，进行坑洞检测的大概算法的实现 
Anthony：最开始的UI设计，地图API的接入，相关地图功能的实现 
Garrio：天眼API的接入使用，配合Anthony完善地图方面功能的调用  
Justin：数据记录、存储、读取与展示，连接数据的存读

> 由于我是懒狗，所以不附上他们的gitHub账户了（他们在该项目的organization里面，如果你想找他们的话）

# RoadConditionTest_EN_Version  
In the cs183 road surface smoothness test in the first semester of MIEC, a map + acceleration sensor was used. very rough

## Welcome to reference research! But please don't copy
Because this code is very basic, in fact, it can be written by one person after learning it for about a month.
If you are a MIEC student and are also preparing to complete the CS183 project, please **DO NOT COPY** to prevent unnecessary implication
If you want to learn from ideas, please refer to the "Technology and Learning" section

## Technology and Learning
1. The relevant implementations on the Android side are all done by AndroidStuio+Java, and the relevant tutorials can actually refer to the official documentation. By the way, the second edition of "The First Line of Code" is the java language, and the subsequent version is the Kotlin language
2. There are still bugs in the database, and the implementation mainly uses the built-in database + SQL link. There are corresponding explanations in "First Line of Code"
3. The map API uses Baidu Map + Sky Eye, and the related usage methods are available on CSDN or Baidu.

## write at the end
I did not complete this project alone, but I completed it together with the project team (four people in total) during my freshman year of study.
RetenQ: UI part optimization, integration project, implementation of rough algorithm for pothole detection
Anthony: Initial UI design, access to map API, implementation of related map functions
Garrio: Access and use of the Sky Eye API, and cooperate with Anthony to improve the call of map functions
Justin: data recording, storage, reading and display, connecting data storage and reading

> Since I'm a lazy dog, I won't attach their gitHub account (they are in the project's organization, if you want to find them)
