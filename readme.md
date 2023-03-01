先发布一个 targetApi 到 29及以下的版本，在这个版本中需要：

如果是 target 到 29 的话，需要在 manifest 文件中添加 android:requestLegacyExternalStorage="true"，这会让系统继续允许App 使用之前的方式访问外部存储。
根据 App 的业务需求通过 File API 把存放在外部存储(/sdcard/)的私有文件迁移到 getExternalFilesDir() 返回的目录下
共享的非媒体文件迁移到 Downloads/ 路径下
迁移完成后删除原文件夹
迁移完成后，再发布一个 TargetApi 到 30 的版本即可。

这个流程中需要注意的：

requestLegacyExternalStorage 在 targetApi 30 后无效，系统会直接忽视。

如果部分老旧版本 App 没有升级到这个 targetApi 29 的过渡版本，而是直接升级到 targetApi 30 的版本，且运行在 Android 11 的设备上，那该如何处理呢？

针对这种情况，API 30 提供了 preserveLegacyStorage 这个flag，设置为 true 之后，如果 App 是升级到 API 30 版本的话，那么requestLegacyExternalStorage 的设置依然有效，可以继续通过上诉流程完成迁移。

preserveLegacyStorage 仅对升级的 App 有效，对于首次安装，或者卸载重新安装无效。