(function(){
    var Timer = {
        saver: {},
        set: function(key, fn, delay) {
            this.saver[key] = setTimeout(fn, delay);
        },
        clear: function(key) {
            clearTimeout(this.saver[key])
        },
        tid: 0,
        setTransitoinEnd: function(node, fn, duration) {
            var id = this.tid++, that = this, callback = function() {
                if (that.saver[id])
                    return;
                fn.call(node);
                that.saver[id] = true;
            };
            that.saver[id] = false;
            $(node).one('webkitTransitionEnd', callback);
            setTimeout(callback, duration);
        }
    },
    
    Gallery = window.Gallery = function(dispatcher, config) {
        this.dispatcher = dispatcher;
        this.config = config;
        this.total = +config.pageCount;
        this.data = {
            /** index: obj **/
        }
        this.el = {
            container: $(config.el)
        };
        this.instance = {};
        this.init();
    };
    
    (function($, Gallery) {
        var that,
        
        Router = Backbone.Router.extend({
            routes: {
                // 'gallery/:index': 'gallery',
                ':index': 'gallery',
                '': 'gallery'
            }
        }),
        
        router = new Router(),
         
        render = function() {
            var tmpl = '<div id="gallery">'+
                '<header id="gallery-header">'+
                    '<a id="gallery-return" href="/photo">返回</a>'+
                    '<span id="gallery-header-index"></span> / <span id="gallery-header-len"></span>'+
                '</header>'+
                '<div id="gallery-slider"></div>'+
                '<div id="gallery-operation">'+
                    '<nav id="gallery-thumbnail"><ul></ul></nav>'+
                '</div>'+
            '</div>';
            that.el.container.append($(tmpl));
            that.el.main = $('#gallery');
            that.el.slider = $('#gallery-slider');        
            that.el.thumbnail = $('#gallery-thumbnail');
            that.el.operation = $('#gallery-operation');
            that.el.header = $('#gallery-header');
            that.el.index = $('#gallery-header-index');
            that.el.length = $('#gallery-header-len').html(that.total);
        },
        
        sliderInstaller = {
            install: function(){
                that.instance.slider = that.el.slider.slider({
                    initIndex: -1
                }, that.dispatcher);
                that.instance.slider.setOption({
                    onBeforeDrag: function(index, e){
                        var result;
                        try{
                            result = that.instance.scale.canSlide(e.gesture.deltaX);
                        }catch(e){
                            result = true;
                        }
                        return result;
                    }
                });
            },
            uninstall: function(){
                that.instance.slider && that.instance.slider.destroy();
            },
            reinstall: function(){
                this.uninstall();
                this.install(); 
            }
        },
        
        thumbnailInstaller = {
            install: function(){
                that.instance.thumbnail = that.el.thumbnail.thumbnail({}, that.dispatcher);
            }
        },
        
        scaleableInstaller = {
            target: null,
            install: function(img) {
                var installer = this,
                offset = {},
                index = $(img)[0].dataset.index;
                if(that.instance.slider.getCurrentIndex() != index) return;
                this.uninstall();
                this.target = img;
                that.instance.scale = this.target.scaleable({
                    doubleTapZoomIn: true,
                    doubleTapZoomRestore: true,
                    dragableOnEnlarged: true,
                    min: .2,
                    slider: that.instance.slider,
                    doubleTapRestoreHandler: $(img).parent()
                }, that.dispatcher);
                that.instance.scale.setOption({
                    onTransformEnd: function(e, scale, relativeScale) {
                        if (scale < 1) {
                            that.instance.scale.restore();
                        }
                    },
                    onTransformStart: function(e, scale, relativeScale) {
                        offset = this.offset();
                        Timer.set('transformEnd', function() {
                            toolbarCtrl.hide();
                        }, 100);
                    }
                });
                that.instance.scale.init();
            },
            uninstall: function() {
                that.instance.scale && that.instance.scale.destroy();
            },
            reinstall: function() {
                that.instance.scale && this.install(this.target);
            }
        },
        
        bindEvent = function(){
            var slider = that.instance.slider,
                thumbnail = that.instance.thumbnail;
                
            that.dispatcher.on('gallery:thumb/itemTap', function(item, newIndex, oldIndex) {
                if (isNaN(newIndex) || newIndex == oldIndex) return;
                slider.moveTo(newIndex, false, false);
                thumbnail.moveTo(newIndex);
                afterSwitchHandler(newIndex, oldIndex, 'thumb');
            });
            
            that.dispatcher.on('gallery:thumb/move', function(startIndex, endIndex, dir){
                fetchData(startIndex, endIndex);
            });
            
            that.dispatcher.on('gallery:slider/afterSwitch', afterSwitchHandler);
            
            that.dispatcher.on('gallery:slider/beforeSwitch', function(newIndex) {
                thumbnail.moveTo(newIndex);
            });
            
            that.dispatcher.on('gallery:scaleable/doubletap', function() {
                Timer.clear('sliderTap');
                toolbarCtrl.hide();
            });
            
            var eventHandler = function(e) {
                switch(e.type) {
                    case 'tap':
                        Timer.set('sliderTap', function() {
                            toolbarCtrl.isHide() ? toolbarCtrl.show(): toolbarCtrl.hide();
                        }, 200);
                        break;
                    case 'dragstart':
                        e.gesture && e.gesture.preventDefault();
                        break;
                    case 'load':
                        $(this).parent().addClass('loaded');
                        break;
                    case 'doubletap':
                        Timer.clear('sliderTap');
                        break;
                }
            }
    
            Hammer(that.el.slider[0]).on('tap', eventHandler);
            Hammer(that.el.main[0]).on('dragstart', eventHandler);
            $(window).on('orientationchange resize', function(e) {
                try {
                    slider.afterResize();
                    thumbnail.afterResize();
                    that.instance.scale.restore(true);
                    scaleableInstaller.reinstall();
                } catch(e) {}
            });
        },
        
        setData = function(data, index, callback){
            var newData = {}
            newData[index] = data;
            that.instance.thumbnail.setData(newData);
            that.instance.slider.setData(newData);
            if(!that.data[index]){
                that.data[index] = data;
            }
            callback && callback();
        },
        
        fetchData = function(startIndex, endIndex, callback){
            if(startIndex != endIndex){
                startIndex = Math.max(startIndex - 5, 0);
                endIndex = Math.min(endIndex + 5, that.total - 1);
            }

            while(startIndex <= endIndex){
                if(!that.data[startIndex]){
                    dataCenter.get(startIndex).then((function(startIndex){
                        return function(data){
                            data.url = 'http://farm'+data.farm+'.staticflickr.com/'+data.server+'/'+data.id+'_'+data.secret+'_b.jpg';
                            data.url_s = 'http://farm'+data.farm+'.staticflickr.com/'+data.server+'/'+data.id+'_'+data.secret+'_q.jpg';
                            setData(data, startIndex, callback);
                        };
                    })(startIndex));
                }else{
                    setData(that.data[startIndex], startIndex, callback);
                }
                startIndex++;
            };
        },
        
        imageLoader = {
            getImg: function(index) {
                var img = new Image(),
                photo = that.data[index],
                url = that.data[index].url;
                img.draggable = false;
                img.dataset.index = index;
                img.src = url;
                return img;
            },
            load: function(index, callback) {
                if (this.valid(index)){
                    var img = this.getImg(index),
                    loadedCallback = function(loadedImg) {
                        var pane = $(that.instance.slider.getPane(index));
                        if (pane.find('img').size())
                            return;

                        if (this.src) {
                            // unloaded
                            that.data[index].loaded = true;
                            that.data[index].loading = false;
                            imageLoader.imgOnLoad(this, pane, index);
                            callback && callback();
                        } else {
                            // loaded
                            // $(loadedImg).on('load', function(){
                                // imageLoader.imgOnLoad(this, pane, index);
                            // });
                        }
                    };
                    
                    $(img).on('load', loadedCallback);
                    that.data[index].loading = true;
                }
            },
            isLoaded: function(index) {
                return that.data[index].loaded;
            },
            isLoading: function(index) {
                return that.data[index].loading;
            },
            preLoad: function(index, offset) {
                while (offset) {
                    this.load(index + offset);
                    this.load(index - offset);
                    offset--;
                }
            },
            valid: function(index) {
                return that.data[index];
            },
            setFunctional: function(from){
                // context: img
                $(this).removeClass('loaded').css('-webkit-transform-origin', '0 0');
                scaleableInstaller.install($(this));
            },
            imgOnLoad: function(img, pane, index){
                if(!$(pane).find('img').size()){
                    $(pane).empty();
                    $(pane).append(img);
                    $(img).addClass('loaded');
                    $(img).one('webkitAnimationEnd', this.setFunctional);
                }
            },
            setLoadedImg: function(img, pane, index, from){
                if (from == 'thumb') {
                    this.imgOnLoad(img, pane, index);
                }else{
                    img = $(that.instance.slider.getCurrentPane()).find('img');
                    
                    // Hack to get the image is in transition or not
                    var isInAnim = getComputedStyle(img[0]).opacity < 1;
                    if(isInAnim){
                        Timer.setTransitoinEnd(img, function(){
                            imageLoader.setFunctional.call(img);
                        }, 400);
                    }else{
                        this.setFunctional.call(img);
                    }
                    
                }
            }
        },
        
        afterSwitchHandler = function(newIndex, oldIndex, from) {
            router.navigate('/'+dataCenter.getId(newIndex));
            that.el.index.html(newIndex + 1);
            try{
                Tip.hideWating();
            }catch(e){}
            that.currentIndex = newIndex;
            var currentPane = $(that.instance.slider.getPane(newIndex)), 
            currentImg = currentPane.find('img');
            scaleableInstaller.uninstall();

            
            if(currentPane.find('img').size()) {
                imageLoader.setLoadedImg(imageLoader.getImg(newIndex), currentPane, newIndex, from);
                imageLoader.preLoad(newIndex, 1);
            }else{
                imageLoader.load(newIndex, function(){
                    imageLoader.preLoad(newIndex, 1);
                });
            }
        },
        
        toolbarCtrl = {
            show: function() {
                that.el.header.removeClass('hide');
                that.el.operation.removeClass('hide');
            },
            hide: function() {
                that.el.header.addClass('hide');
                that.el.operation.addClass('hide');
            },
            isHide: function() {
                return that.el.operation.hasClass('hide');
            }
        };
    
        $.extend(Gallery.prototype, {
    
            init: function() {                
                that = this;
                render();
    
                thumbnailInstaller.install();
                sliderInstaller.install();
                bindEvent();
    
     
                that.instance.slider.init();
                that.instance.thumbnail.init(that.config.pageCount);
                
                
                that.instance.slider.setData({}, that.config.pageCount);
                that.instance.thumbnail.setData({});
                
                
                router.on('route:gallery', function(id) {
                    var index = 0;
                    if(id){
                        index = dataCenter.getIndex(id);
                    }else{
                        index = dataCenter.getIndex(that.config.primary);
                    }
                    that.goToPage(index);
                });
                Backbone.history.start();
                
            },
    
            goToPage: function(index) {
                index = +index;
                if(isNaN(index) || index < 0 || index >= this.total || this.currentIndex == index) return;
                that.el.index.html(index + 1);
                
                fetchData(index, index, function(){
                    that.instance.thumbnail.moveTo(index);
                    that.instance.slider.moveTo(index);
                });
                
                that.instance.thumbnail.moveTo(index);
            },
    
            nextPage: function() {
                if(this.instance.slider.hasNext())
                    this.instance.slider.next();
                else
                    this.instance.slider.immovableHint();
            },
    
            prevPage: function() {
                if(this.instance.slider.hasPrev())
                    this.instance.slider.prev();
                else
                    this.instance.slider.immovableHint();
            },
    
            showThumbnail: function() {
                toolbarCtrl.show();
            },
    
            hideThumbnail: function() {
                toolbarCtrl.hide();
            },
    
            toggleThumbnail: function() {
                if(toolbarCtrl.isHide())
                    toolbarCtrl.show();
                else
                    toolbarCtrl.hide();
            },
            
            getCurrentPage: function(){
                return this.currentIndex;
            }
            
        });
    })(Zepto, Gallery);
})();