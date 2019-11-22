## Feature
- Easy to use
- Support various input: UrlString, File, Resource, Uri
- Support local & network DataSource
- Resizing image
- Basic scale transformation: Center Crop, Fit Center
- Caching image for faster reuse

## Install

Add these line in your build.gradle at the end of repository
```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }  
}
```

Add the dependency
```gradle
dependencies{
  implementation 'com.github.kim-ninh:faster:v1.0'
}
```

v1.0
## Usage

### Common use

This will create request with scale type is match the imageView's scale type. Placeholder loading is disable by default. And the size of target image with automatic get from ImageView's width, height

```java
void onCreate(){

    // ...

    Faster.with(context)
        .load(urlString)
        .into(imageView);
}
```

or with some tweak

```java
void onCreate(){
    // ...

    Faster.with(context)
        .load(urlString)
        .placeholder(new ColorDrawable(Color.TRANSPARENT))
        .transfrom(ScaleType.FIT_CENTER)
        .into(imageView)    
    
}
```

### RecyclerView, ListView

```java
public void onBindViewHolder(ViewHolder vh, int position){

    Faster.with(PhotoGalleryFragment.this.getActivity())
            .load(itemURL)
            .placeholder(new ColorDrawable(Color.TRANSPARENT))
            .into(holder.mItemImageView);
}
```
