(function($) {
    
    var Waterfall = function(options) {
        this.options = $.extend({
            autoCenter: true
        }, options);
        
        this.container = $(this.options.container);
        this.itemCollection = this.container.children();
        this.resize();
    };
    
    $.extend(Waterfall.prototype, {
        layout: function(els) {
            var i,
                els = $(els),
                that = this;
            els.each(function(index, item){
                that.setPos( that.getPos(item) );
            });
            this.container.css('height', Math.max.apply(Math, this.coorsY));
        },

        updateCols : function() {
            var i = 0,
                containerWidth = this.containerWidth = this.container.width();
            this.columnWidth = this.options.columnWidth;
            this.cols = Math.floor(containerWidth / this.columnWidth);
            this.coorsY = [];
            while (this.cols > i++)
                this.coorsY.push(0);
        },
        
        setPos: function(param){
            $(param.el).css('-webkit-transform', 'translate3d('+ param.x +'px, '+ param.y +'px, 0)');
        },
        
        getPos: function(el) {
            el = $(el);
            var groupColY,
                posX,
                posY = Math.min.apply(Math, this.coorsY),
                colIndex = 0;
            
            this.coorsY.some(function(item, index){
                colIndex = index;
                return item == posY;
            });
            posX = this.columnWidth * colIndex;
            
            if(this.options.autoCenter){
                posX += (this.containerWidth - this.columnWidth * this.cols) / (this.cols + 1) * (colIndex + 1);
            }
            
            this.coorsY[colIndex] = posY + el.height();

            return {
                el: el,
                x: posX,
                y: posY
            }
        },

        resize: function() {
            this.updateCols();
            this.layout(this.itemCollection);
        },

        append: function(el) {
            this.container.append(el);
            this.itemCollection = this.itemCollection.add(el);
            this.layout(el);
        }
    });
    
    $.Waterfall = Waterfall;
    
})(Zepto);
