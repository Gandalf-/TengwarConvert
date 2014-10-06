#lang racket

;Gui for TengwarConvert.java

;OUTLINE
;----------------------------------------------------
; GUI gets input from user
; GUI creates Output.txt with input
; GUI calls TengwarConvert.java and passes Output.txt
; TengwarConvert.java runs conversion
; TengwarConvert prints output to Output_converted.txt
; GUI calls OpenOutput.vbs
; OpenOutput.vbs opens Output_converted.txt in notepad
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

;INPUT
(define input "in aenchent tiems the rings of power wer crafted bie the elven-smiths, and sawron, the dark lord, forjed the wun ring, filling it with hiz oewn power so that he culd rul all others!")

(print-this input "Output.txt")

(define output
  (with-output-to-string (lambda ()
                           (system "java TengwarConvert Output.txt"))))

(system "cscript OpenOutput.vbs")

(delete-file "Output_converted.txt")
(delete-file "Output.txt")



(send main-frame create-status-line)
;(send main-frame show #t)