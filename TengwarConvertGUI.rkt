#lang racket

;Gui for TengwarConvert.java
; 

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