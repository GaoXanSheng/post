# POST 

#### 介绍
Minecraft 1.12 开源 向目标服务器POST内容，返回指令进行执行


#### 使用说明

1.  构建项目
2.  放入plugins文件夹
3.  /post url value
4. 指定协议"Content-Type", "application/json"
5. POST 内容 
```
player
world
value
UUID
Level
X
Y
Z
```
6.返回值解析，返回需要一个JSON
```
Key CMD
value 执行指令 
例如
{"CMD":"op player"}
```
#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


