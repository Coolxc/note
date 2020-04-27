`git init`：初始化git仓库，创建.git目录

设置签名：区分不同的开发人员

> 这里设置的签名和github账号密码没有任何关系，可以任意指定
>
> **项目级别（仓库级别）：**仅在当前本地库范围内生效
>
> > `git config user.name yang_you_xiu`
> >
> > `git config user.email 131@qq.com`
>
> **系统用户级别：**登录当前系统的用户范围，比项目级别范围大
>
> > `git config --global user.name yang_you_xiu`
> >
> > `git config --global user.email 131@qq.com`
>
> **优先级：**就近原则，项目级别优先于用户级别。二者都有时采用项目级别的签名。如果只有用户级别的签名则以用户级别的签名为准。二者都没有不允许
>
> `cat .git/config`
>
> ```properties
> [core]
>         repositoryformatversion = 0
>         filemode = true
>         bare = false
>         logallrefupdates = true
> [user]
>         name = yang
>         email = 1312045515@qq.com
> ```
>
> `cat ~/.gitconfig`：查看系统用户级别的信息

```powershell
yang@PC-20200329MWQL:~/gitDemo/weChat$ git status  #查看工作区、暂存区状态
On branch master

No commits yet   #还没有任何可以提交的东西，并且下面提示可以使用git add来追踪一个文件

nothing to commit (create/copy files and use "git add" to track)
yang@PC-20200329MWQL:~/gitDemo/weChat$ vim hello.sh
yang@PC-20200329MWQL:~/gitDemo/weChat$ git status
On branch master

No commits yet

Untracked files:  #提示有一个文件未被追踪，使用git add 将工作区的文件添加到暂存区。git会追踪暂存区的文件
  (use "git add <file>..." to include in what will be committed)

        hello.sh

nothing added to commit but untracked files present (use "git add" to track)
yang@PC-20200329MWQL:~/gitDemo/weChat$ git add hello.sh
yang@PC-20200329MWQL:~/gitDemo/weChat$ git status
On branch master

No commits yet

Changes to be committed:  #有一个更改需要被提交，也可以使用rm --cached来从暂存区删除
  (use "git rm --cached <file>..." to unstage)

        new file:   hello.sh
```

`git commit`：提交所有在暂存区的文件。会进入一个编辑框，输入本次提交的注释信息。

**提交后当此文件再发生修改时** 

```shell
no changes added to commit (use "git add" and/or "git commit -a")
```

此时提示可以直接使用`git commit`而不用再add了。

可以加一个-m就不用再进入编辑器输入注释信息了`git commit -m "haha" hello.sh`

##### 版本控制

`git log`

> ```yml
> Author: yang <1312045515@qq.com>
> Date:   Thu Apr 16 16:01:26 2020 +0800
> 
>     modify
> 
> commit d13bdcf3c5c7f94ecc4a46488cf2e30c46d37054
> Author: yang <1312045515@qq.com>
> Date:   Thu Apr 16 15:44:41 2020 +0800
> 
>     touch a new file hello.sh
> ```
>
> 自定义显示样式
>
> > ```powershell
> > yang@PC-20200329MWQL:~/gitDemo/weChat$ git log --pretty=oneline
> > 70f08a3125c3bac189a2ee0731cf713e0088db5d (HEAD -> master) modify
> > d13bdcf3c5c7f94ecc4a46488cf2e30c46d37054 touch a new file hello.sh
> > #reflog可以显示出版本号
> > yang@PC-20200329MWQL:~/gitDemo/weChat$ git reflog
> > 70f08a3 (HEAD -> master) HEAD@{0}: commit: modify
> > d13bdcf HEAD@{1}: commit (initial): touch a new file hello.sh
> > ```
> >
> > 也可以`git log --oneline`

##### 前进与回退版本

选择版本：`git reset --hard 70f08a3`  最后的是版本号

##### 比较文件

`git diff 文件名`：将工作区的文件与暂存区进行比较

`git diff 本地库历史版本指针 文件名`：与指定版本进行比较

##### 分支

创建分支：`git branch hot_fix`

查看所有分支：`git branch`

切换分支：`git checkout hot_fix`

在分支上修改后需要切换到master主分支上进行merge

```powershell
yang@PC-20200329MWQL:~/gitDemo/weChat$ git checkout master
Switched to branch 'master'
yang@PC-20200329MWQL:~/gitDemo/weChat$ git merge
fatal: No remote for the current branch.
yang@PC-20200329MWQL:~/gitDemo/weChat$ git merge hot_fix #合并hot_fix分支
Updating 70f08a3..a436d6f
Fast-forward
 hello.sh | 1 +
 1 file changed, 1 insertion(+)
```

**冲突**

> ```shell
> #当两个分支同时修改同一个文件的同一行时就会发生冲突，git不知道要提交谁的修改
> $git checkout hot_fix #切换到hot_fix分支
> $vim hello.sh  #修改文件
> $git commit -m "hot_fix" hello.sh  #提交修改
> 
> $git checkout master #切换到master分支
> $vim hello.sh  #修改文件
> $git commit -m "master_modify" hello.sh
> 
> $git branch -v
> hot_fix 1aea18c hot_fix
> * master  650937a master_modify  #当前在master分支
> 
> #此时合并hot_fix分支就会失败，git不知道要应用谁的修改
> $git merge hot_fix
> Auto-merging hello.sh
> CONFLICT (content): Merge conflict in hello.sh
> #提示修复冲突然后再提交
> Automatic merge failed; fix conflicts and then commit the result.
> 
> #此时查看hello.sh文件
> echo "aaa"
> <<<<<<< HEAD
> echo "hot_master"
> =======   #等号分割两个分支的内容，处理冲突可以根据自己需要编辑内容。然后将git插入的特殊符号（<<<<<<< HEAD；=======；>>>>>>> hot_fix）删除即可
> echo "hot_fixX"
> >>>>>>> hot_fix
> 
> #删除后的hello.sh文件，这里选择了同时保留两个分支的修改
> echo "aaa"
> echo "hot_master"
> echo "hot_fixX"
> 
> $git status  #此时查看git状态会发现需要执行add命令来标记冲突已解决
> Unmerged paths:
>   (use "git add <file>..." to mark resolution)
>   
> $ git add hello.sh
> $ git status
> On branch master
> All conflicts fixed but you are still merging.
>   (use "git commit" to conclude merge)  #使用git commit来彻底解决冲突
> 
> Changes to be committed:
> 
>         modified:   hello.sh
>         
> $ git commit -m "conclude merge"  #解决冲突，不能带文件名
> [master 6688ae8] Merge branch 'hot_fix'
> $ git status  #OK
> On branch master
> nothing to commit, working tree clean
> ```
>
> **冲突的解决总结**
>
> > 1. 编辑冲突文件，删除特殊符号
> > 2. 把文件修改到满意的程度，保存退出
> > 3. `git add 文件名`
> > 4. `git commit -m "日志信息"` 。这个commit不能加文件，直接使用就行

##### 推送到远程仓库

`git remote add demo https://github.com/YangQQ1312045515/demo.git`：添加远程仓库地址

`git push demo master`：将master分支推送到demo仓库，demo指向了我们定义的仓库地址

`git clone 远程仓库地址`：克隆远程库

> `git clone https://github.com/YangQQ1312045515/demo.git`有三个效果
>
> > 完整的把远程库克隆到本地
> >
> > 创建demo远程地址别名，也就是说自动的执行了`git remote add demo https://github.com/YangQQ1312045515/demo.git`
> >
> > 初始化本地库

##### 拉取

> `pull=fetch+merge`
>
> `git fetch [远程库别名] [远程分支名]`
>
> `git merge [远程库地址别名/远程库分支名]`
>
> pull可以一步到位，fetch是先将内容拉取到本地库，查看后在进行merge将远程库分支合并到本地库

##### 团队协作

![image-20200417094206825](C:\Users\Administrator\Desktop\oooooo\Git\图片\image-20200417094206825.png)

##### GIT工作流

![image-20200417102055253](C:\Users\Administrator\Desktop\oooooo\Git\图片\image-20200417102055253.png)