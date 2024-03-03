# RealmSandbox

Запустить приложение можно открыв проект Realmsandbox в Android Studio и запустив его на эмуляторе или устройстве.

Альтернативный способ запуска без Android Studio:
```bash
cd Realmsandbox
./gradlew build
./gradlew installDebug
adb -d shell am start -n com.skygrel19.realmsandbox/.MainActivity
```