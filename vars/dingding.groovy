package org.devops

def GetChangeString() {
    MAX_MSG_LEN = 100
    def changeString = ""
    def changeLogSets = currentBuild.changeSets
    for (int i = 0; i < changeLogSets.size(); i++) {
        def entries = changeLogSets[i].items
        for (int j = 0; j < entries.length; j++) {
            def entry = entries[j]
            truncated_msg = entry.msg.take(MAX_MSG_LEN)
            commitTime = new Date(entry.timestamp).format("yyyy-MM-dd HH:mm:ss")
            changeString += "> - ${truncated_msg} [${entry.author} ${commitTime}]\n"
        }
    }
    if (!changeString) {
        changeString = "> - No new changes"
    }
    return changeString
}

def DingdingReq(RobotID, Status) {
   // wrap([$class: 'BuildUser']) {
        def changeString = GetChangeString()
        dingtalk (
            robot: RobotID,
            type: 'MARKDOWN',
            title: '你有新的消息，请注意查收',
            text: [
                "### 构建信息",
                "> - 应用名称：**${env.JOB_NAME}**",
                "> - 构建结果：**${Status}**",
                "> - 当前版本：**${env.BUILD_NUMBER}**",
                "> - 构建发起人：**${env.BUILD_USER}**",
                "> - 持续时间：**${currentBuild.durationString}**",
                "> - 构建日志：[点击查看详情](${env.BUILD_URL}console)",
                "### 更新记录:",
                "${changeString}"
            ],
            at: [
                "马一凡 13119347914"
            ]
        )
  //  }
}
