function sniff(){
    var path = '//squizlabs.github.io/HTML_CodeSniffer/build/';
    var msgArray=[];
    var HTMLCS_RUNNER = new function() {
        this.run = function(standard, callback) {
            var self = this;

            HTMLCS.process(standard, document, function() {

                var messages = HTMLCS.getMessages();
                var length   = messages.length;
                var msgCount = {};
                msgCount[HTMLCS.ERROR]   = 0;
                msgCount[HTMLCS.WARNING] = 0;
                msgCount[HTMLCS.NOTICE]  = 0;

                for (var i = 0; i < length; i++) {
                    msgArray[i]=self.output(messages[i]);
                    msgCount[messages[i].type]++;
                }
            }, function() {
                return msgArray;
            }, 'en');
        };

        this.output = function(msg) {


            var typeName = 'UNKNOWN';
            switch (msg.type) {
            case HTMLCS.ERROR:
                typeName = HTMLCS.getTranslation("auditor_error");
                break;

            case HTMLCS.WARNING:
                typeName = HTMLCS.getTranslation("auditor_warning");
                break;

            case HTMLCS.NOTICE:
                typeName = HTMLCS.getTranslation("auditor_notice");
                break;
            }

            var nodeName = '';
            if (msg.element) {
                nodeName = msg.element.nodeName.toLowerCase();
            }

            var elementId = '';
            if (msg.element.id && (msg.element.id !== '')) {
                elementId = '#' + msg.element.id;
            }


            var html = '';
            if (msg.element.outerHTML) {
                var node = msg.element.cloneNode(true);
                node.innerHTML = '...';
                html = node.outerHTML;
            }
            var messagemap = new Map([["typeName" ,typeName],
                    ["Code", msg.code], ["nodeName", nodeName], ["elementId", elementId], ["message", msg.msg], ["html", html]]);
             };
        return msgArray;
    };

    var scriptSetup = function(s, cb) {
        var sc = document.createElement('script');
        sc.onload = function() {
            sc.onload = null;
            sc.onreadystatechange = null;
            cb.call(this);
        };
        sc.onreadystatechange = function() {
            if (/^(complete|loaded)$/.test(this.readyState) === true) {
                sc.onreadystatechange = null;
                sc.onload();
            }
        };
        sc.src = s;
        if (document.head) {
            document.head.appendChild(sc);
        } else {
            document.getElementsByTagName('head')[0].appendChild(sc);
        }
        return msgArray;
    };
    var options = {
        path: path
    };
    scriptSetup(path + 'HTMLCS.js', function() {
      HTMLCS_RUNNER.run('WCAG2AA');
    });
    console.log(msgArray);
    return msgArray;
};