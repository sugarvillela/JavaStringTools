# JavaStringTools:
  - Matcher
  - Tokenizer
  - Splitter
  - Replacer
  - Unwrapper (trim quotes or brackets)
  - Validator (for Unwrapper)
  - Indent Counter
  - Tab-to-space filter (TabUtil)   
    
## Features:
  - Char or word match
  - Multiple delimiters (char match)
  - Skip areas (Deliniated by special characters like quotes, brackets etc)
  - Ignores adjacent delimiters
  - Provides list of match indices
  - Obeys escape character
  - Option to keep delimiters as tokens
  - Option to defer action until a given index is reached
  - Option to limit number of actions  
  - Option to match case-insensitive
  
## Input:
  - Text
  - Delimiters
  - Skip Symbols
  - Options
  - All set with a builder-style pattern
  
  ## Output:
  - String Array or String List for tokens
  - Int Array or Integer List for found indices
  - Int for found size
  - String for getting modified text
  
See tests for detailed usage.   
    
## Overview:
Although string tasks are varied, they all involve mapping the location of particular    
characters in a string. Thus every tool in this project is based on one of three match    
algorithms: either char match, word match or pair match (for quotes, brackets etc). 
    
Char match takes multiple delimiters as a string.
  - If any character in the string is found, it is mapped as a delimiter    
  
Word match takes one sequence of characters as a string.
  - If the entire sequence is found, its start and end points are mapped as a delimiter  
  
Pair match takes multiple delimiters as a string.    
  - The list of delimiters should only include the first part of the pair: (, {, [, ', ".
  - The closing symbol is surmised from the opening character: ), }, ], ', ".
  - If the character is not on the list, the same character becomes the closer: as in ', ".   
  
Implementation:
  - The tools described all implement the IStringParser interface
  - The 'impl' package contains the basic match classes
  - The 'composite' package contains first-level composites
  - The 'composite2' package uses composites of composites
  - 'simpl' contains 2 stripped down implementations having little to do with the rest of it. 
  - Although both array/list outputs are provided, one is more efficient, depending on the algorithm.
  
### About Skip Symbols:
When you tokenize a string, you may want to leave quoted or bracketed areas whole.    
Thus the algorithm provides a way to define skip areas by their deliniating symbols.    
The symbols tend to be the same pairs of symbols in the list above, so the same method    
is used to define them: just pass the list of opening symbols and the area will be skipped.  
  - Using the same characters as delimiters and skip symbols causes undetermined behavior.    
  
## Notes on Usage:
### Matchers
  - Matchers only report found indices so only the numeric outputs are meaningful
  - Matchers' String array and String list return null.
  - All other tools will have string output plus the internal matcher's numeric output
### Tokenizer
  - If your delimiter is something more interesting than a space, you can save to its own element
### Splitter
  - Just a tokenizer with a hard limit and a guaranteed output array size
  - Unused elements are padded with null
  - Useful for tokenizing during concurrent modification (final length is unknown so you break off pieces until empty)
### Replacer
  - Obviously you can delete matched text by replacing with an empty String
### Unwrapper
  - Input: 'myText' Output: myText
  - Not fooled by non-wrapping text: Input 'myText1' and 'myText2' Output 'myText1' and 'myText2'
  - Nesting okay: input: {'(a),(b)'}  output: (a),(b)
### Validator
  - Validation only relevant for Pair Matching
  - Catches orphan or unclosed symbols: Input: {(myText) Output: error
### Indent Counter
  - A decorator for tokenizer that tells you how many spaces were removed
  - Useful for recreating a source string from tokenized text, with spacing intact
  - Contains one method not defined in IStringParser interface: getIndentMap()
  - Extra method keeps other Integer outputs at their original purpose
### Tab-to-space filter
  - Converts a tab character to four spaces
  - Run this before tokenizing with the Indent Counter
### SpaceUtil
  - If multiple consecutive spaces exist in a String, converts all to single
  - Preserves indent at beginning of string
