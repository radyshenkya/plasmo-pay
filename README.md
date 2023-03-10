# Plasmo Pay Mod
**Plasmo Pay** - мод для *Minecraft [Fabric](https://fabricmc.net/) 1.19.2*, который добавляет возможность переводить алмазы с карт [Plasmo RP](https://rp.plo.su/) не заходя на сайт.

# Установка
Нужно скачать с вкладки [releases](https://github.com/radyshenkya/plasmo-pay/releases) последнюю версию мода, и закинуть ее в папку **.minecraft/mods**
(как и для всех остальных модов в майнкрафтике.)

# Использование
## Открыть диалог для перевода
Диалог для перевода средств открывается на кнопку **U** (Это кнопку можно изменить в настройках.).

После нажатия у вас откроется вот такое окошко (закрыть его можно на **Esc**):

<img src="https://user-images.githubusercontent.com/52829258/222184310-b1916e23-fc3f-49f5-8d32-4676f1721535.png" width=500px />

Вот что значит каждое из полей (в меню идут в таком же порядке, при наведении мышкой отобразится имя поля):
* **На какую карту** - номер карты, на которую нужно перевести денег (можно без `EB-`)
* **Сумма перевода** - кол-во алмазов, которое нужно перевести. (Писать только число.)
* **Сообщение к переводу** - сообщение к переводу
* **С какой карты** - номер вашей карты, с которой нужно перевести денег (можно без `EB-`) (Последняя использованная карта сохраняется)

Перед использованием вам нужно будет **Установить токен**. [Вот ссылка на инструкцию](https://github.com/radyshenkya/plasmo-pay/blob/main/TOKEN.md).
*P.S: Последний токен сохраняется, так что вводить каждый раз его не нужно.*

После успешного ввода токена, можно прописать здесь данные для перевода:

<img src="https://user-images.githubusercontent.com/52829258/222184818-19f0fe05-d438-4ceb-b2bd-9e34ea0aba95.png" width=500px />

После этого нужно нажать **Перевести.**
Если все прошло успешно, будет выведено сообщение об этом:

<img src="https://user-images.githubusercontent.com/52829258/222184959-d591c12a-b8b1-469f-9c68-6059885d98da.png" width=500px />

А на сайте плазмо появится новый перевод:

<img src="https://user-images.githubusercontent.com/52829258/222185114-dca40fb7-8545-471e-ba6f-480afd8026d1.png" width=500px />



## Создание таблички для перевода
Для удобства, можно создать табличку, с которой окно перевода будет автоматически заполнятся нужными данными. (Например: в вашем магазинчике создать такую табличку, что бы можно было быстро ввести номер вашей карты с нужным кол-вом алмазов и комментарием для перевода.)

Для использования этой функции вам нужно установить на сервере **табличку** в таком формате:
1. PlasmoPay
2. EB-0000
3. 1
4. Comment

Где:
* `PlasmoPay` - эта строка всегда должна быть именно такой. Это как триггер для мода
* `EB-0000` - карта, на которую будет произведен перевод (можно писать без `EB-`).
* `1` - кол-во алмазов, которое будет переведено (Писать только число, а то не будет работать.)
* `Comment` - комментарий к переводу

Пример:

<img src="https://user-images.githubusercontent.com/52829258/220256624-33fefb6c-d462-4070-83d7-6b38aa7576e3.png" width=500px />

Для использования этой таблички достаточно кликнуть по ней **ПКМ**.

После этого откроется меню перевода с уже заполненными данными:

<img src="https://user-images.githubusercontent.com/52829258/222185440-912168d0-40b0-49de-9b28-5688faec411e.png" width=500px />
