#lang racket

;Gui for TengwarConvert.java

;OUTLINE
;----------------------------------------------------
; GUI gets input from user
; GUI creates Output.txt
; GUI calls TengwarConvert.java and passes input
; TengwarConvert.java runs conversion
; GUI gets output from TengwarConvert.java
; GUI saves output to Output.txt
; GUI calls OpenOutput.vbs
; OpenOutput.vbs opens Output.txt in notepad
; OpenOutput.vbs changes font to Tengwar Annatar

(require racket/gui)

;FILE IO
;-----------------------------------------------------
;Moves the file into a string
(define (file->listChars filename)
  (port->string (open-input-file filename)))

;Prints x to a file
(define (print-this x name)
  (call-with-output-file* name #:exists 'replace
                          (lambda (output-port)
                            (display x output-port))))

;MAIN-WINDOW
;---------------------------------------------
(define main-frame 
  (new frame%
       (label "TengwarConvert")
       (stretchable-width #f)
       (stretchable-height #f)
       ))

(define panel (instantiate horizontal-panel% (main-frame)
                (stretchable-height #f)
                ))


(send main-frame create-status-line)
(send main-frame show #t)