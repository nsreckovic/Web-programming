package server;

public class HtmlPage {

    // Code used from: https://codepen.io/kami441/pen/xardn

    protected static String returnQuotePage (Quote quote) {
        System.out.println("[HTML page]:" + quote.toString());
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"en\">\n");

        sb.append("<head>\n");

            sb.append("<meta charset=\"UTF-8\">\n");
            sb.append("<title>Quote of the day</title>\n");
            sb.append("<style>\n");
            sb.append("body {\n" +
                    "            background : repeat rgba(235, 150, 108, 0.1);\n" +
                    "            padding: 3em;\n" +
                    "        }\n" +
                    "        .main {\n" +
                    "            position: relative;\n" +
                    "        }\n" +
                    "        .mb-wrap {\n" +
                    "            margin: 20px auto;\n" +
                    "            padding: 20px;\n" +
                    "            position: relative;\n" +
                    "            width: 300px;\n" +
                    "        }\n" +
                    "        .mb-wrap p {\n" +
                    "            margin: 0;\n" +
                    "            padding: 0;\n" +
                    "        }\n" +
                    "        .mb-wrap blockquote {\n" +
                    "            margin: 0;\n" +
                    "            padding: 0;\n" +
                    "            position: relative;\n" +
                    "        }\n" +
                    "        .mb-wrap cite {\n" +
                    "            font-style: normal;\n" +
                    "        }\n" +
                    "        .mb-style-2 blockquote {\n" +
                    "            padding-top: 150px;\n" +
                    "        }\n" +
                    "        .mb-style-2 blockquote:after {\n" +
                    "            background: none repeat scroll 0 0 rgba(235, 150, 108, 0.8);\n" +
                    "            border-radius: 50% 50% 50% 50%;\n" +
                    "            color: rgba(255, 255, 255, 0.5);\n" +
                    "            content: \"❞\";\n" +
                    "            font-family: 'icons';\n" +
                    "            font-size: 70px;\n" +
                    "            height: 130px;\n" +
                    "            left: 50%;\n" +
                    "            line-height: 130px;\n" +
                    "            margin-left: -65px;\n" +
                    "            position: absolute;\n" +
                    "            text-align: center;\n" +
                    "            text-shadow: 0 1px 1px rgba(255, 255, 255, 0.1);\n" +
                    "            top: 0;\n" +
                    "            width: 130px;\n" +
                    "        }\n" +
                    "        .mb-style-2 blockquote:before {\n" +
                    "            border-left: 5px solid rgba(235, 150, 108, 0.1);\n" +
                    "            border-radius: 50% 50% 50% 50%;\n" +
                    "            content: \"\";\n" +
                    "            height: 500px;\n" +
                    "            left: -50px;\n" +
                    "            position: absolute;\n" +
                    "            top: 0;\n" +
                    "            width: 500px;\n" +
                    "            z-index: -1;\n" +
                    "        }\n" +
                    "        .mb-style-2 blockquote p {\n" +
                    "            background: none repeat scroll 0 0 rgba(255, 255, 255, 0.5);\n" +
                    "            box-shadow: 0 -6px 0 rgba(235, 150, 108, 0.2);\n" +
                    "            color: rgba(235, 150, 108, 0.8);\n" +
                    "            display: inline;\n" +
                    "            font-family: Baskerville, Georgia, serif;\n" +
                    "            font-style: italic;\n" +
                    "            font-size: 28px;\n" +
                    "            line-height: 46px;\n" +
                    "            text-shadow: 0 1px 1px rgba(255, 255, 255, 0.5);\n" +
                    "        }\n" +
                    "        .mb-attribution {\n" +
                    "            text-align: right;\n" +
                    "        }\n" +
                    "        .mb-author {\n" +
                    "            color: #D48158;\n" +
                    "            font-size: 18px;\n" +
                    "            font-weight: bold;\n" +
                    "            padding-top: 10px;\n" +
                    "            text-shadow: 0 1px 1px rgba(255, 255, 255, 0.1);\n" +
                    "            text-transform: uppercase;\n" +
                    "        }\n" +
                    "        .mb-date {\n" +
                    "            color: #D7AA94;\n" +
                    "            font-style: italic;\n" +
                    "        }\n");
            sb.append("</style>\n");

        sb.append("</head>\n");

        sb.append("<body>\n");

            sb.append("<section class=\"main\">\n");

                sb.append("<div class=\"mb-wrap mb-style-2\">\n");
                    sb.append("<blockquote>\n");
                        sb.append("<p>" + quote.getQuote() + "</p>");
                    sb.append("</blockquote>\n");
                sb.append("</div>");

                sb.append("<div class=\"mb-attribution\">\n");
                    sb.append("<p class=\"mb-author\">\n");
                        sb.append(quote.getAuthor());
                    sb.append("</p>\n");
                    sb.append("<p class=\"mb-date\">\n");
                        sb.append(quote.getDate());
                    sb.append("</p>\n");
                sb.append("</div>\n");

            sb.append("</section>\n");

        sb.append("</body>\n");
        sb.append("</html>");
        return sb.toString();
    }

}
