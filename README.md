wordfreq2.java parses through a text and uses a red-black tree (RBTree.java) to store the frequencies of each word in the text, with the word and the frequency being the key-value pair.
To run wordfreq2.java, pass a text file as the only argument. The program then will prompt you to enter a word, with which you can use the following commands:

`>word`	- returns the word that comes after "word".

`<word`	- returns the word that comes before "word".

`<` 	- returns the alphabetically-first word in the text.

`>`	- returns the alphabetically-last word in the text.

`>` -word	- deletes "word" from the tree.

Example output:

> This text contains 2629 distinct words.
> Please enter a word to get its frequency, or hit enter to leave.
a

> "a" appears 632 times.

>a

> The word "abide" comes after "a".

<a

> "a" is the first word alphabetically.

-a

> "a" has been deleted.

a

> "a" does not appear.

>

> The alphabetically-last word in the text is "zigzag".
