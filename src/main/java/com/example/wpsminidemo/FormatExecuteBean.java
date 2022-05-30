/**
  * Copyright 2022 bejson.com 
  */
package com.example.wpsminidemo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Auto-generated: 2022-05-30 14:19:27
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
@Accessors(chain = true)
public class FormatExecuteBean {
    private String src_uri;
    private String file_name;
    private String export_type;
    private String task_id;
    private boolean long_pic;
    private boolean long_txt;
    private boolean hold_line_feed;
    private String password;
    private boolean show_comments;
    private int from_page;
    private int to_page;
    private int scale;
    private int quality;
    private int sheet_count;
    private int sheet_index;
    private int fit_to_width;
    private int fit_to_height;
    private boolean is_horizontal;
    private int paper_size;
    private boolean first_page;
    private int max_sheet_col;
    private int max_sheet_row;
    private int multi_page;
    private int slim_type;




}