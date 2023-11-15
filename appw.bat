@echo off
set JLINK_VM_OPTIONS=
set DIR=%~dp0
"%DIR%\javaw" %JLINK_VM_OPTIONS% -m com.github.maeda6uiui.miffie/com.github.maeda6uiui.miffie.MiffieApp %*
